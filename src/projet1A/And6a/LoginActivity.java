package projet1A.And6a;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import projet1A.And6a.http.HTTPJson;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// On récupère le salt
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String salt = prefs.getString("salt", "");
		Log.d("And6@ - LoginActivity", "pref SALT = " + salt);

		// Si le salt existe
		if (salt.length() > 0) 
			this.tryToConnect(salt); // On essaye de se connecter
	}

	public void onClick(View view)
	{
		if (view.getId() == R.id.login) {

			EditText user = (EditText) findViewById(R.choix.user);
			String userStr = user.getText().toString();

			EditText pass = (EditText) findViewById(R.choix.pass);
			String passStr = pass.getText().toString();

			if (!userStr.equals("") && !passStr.equals("")) 
				this.tryToConnect(userStr, URLEncoder.encode(passStr));; // On essaye de se connecter
		}
	}

	public void tryToConnect(String salt)
	{
		String url = "http://api.trombi.iariss.fr/login/salt/" + salt;

		// On teste la conexion
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((conMan.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED) || (conMan.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)) {
			Log.d("And6@ - LoginActivity", "INTERNET - oui");
			Connection connection = new Connection(this);
			connection.execute(url);
		}
		else {
			Log.d("And6@ - LoginActivity", "INTERNET - non");
			Toast.makeText(getApplicationContext(), "Vous n'avez pas d'accès à internet.", Toast.LENGTH_LONG).show();
		}
	}

	public void tryToConnect(String userStr, String passStr)
	{

		String url = "http://beta.trombi.iariss.fr/rest/login/" + userStr + "/" + passStr;

		// On teste la conexion
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((conMan.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED) || (conMan.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)) {
			Log.d("And6@ - LoginActivity", "INTERNET - oui");
			Connection connection = new Connection(this);
			connection.execute(url);
		}
		else {
			Log.d("And6@ - LoginActivity", "INTERNET - non");
			Toast.makeText(getApplicationContext(), "Vous n'avez pas d'accès à internet.", Toast.LENGTH_LONG).show();
		}
	}

	private class Connection extends AsyncTask<String, Void, JSONObject>
	{

		private ProgressDialog progressDialog;
		private Context context;

		Connection(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			this.progressDialog = new ProgressDialog(this.context);
			this.progressDialog.setMessage("Connexion en cours...");
			this.progressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... url) {
			JSONObject result = null;
			try {

				result = HTTPJson.doPost(url[0]);
				Log.d("And6@ - LoginActivity", "result = " + result.toString());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - LoginActivity", e.getMessage());
			}
			return result;
		}

		protected void onPostExecute(JSONObject result) {

			this.progressDialog.dismiss();

			try {

				if (result.optString("error").equals("bad_password"))
					Toast.makeText(this.context, "Mauvais mot de passe", Toast.LENGTH_LONG).show();
				else if (result.optString("error").equals("bad_login"))
					Toast.makeText(this.context, "Mauvais login", Toast.LENGTH_LONG).show();
				else {
					// On enregistre la connexion
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("salt", result.getString("salt"));


					// On récupère les informations personnelles
					editor.putString("class", result.getString("class"));
					editor.putString("year", result.getString("year"));
					editor.putString("fullname", result.getString("fullname"));

					editor.putLong("timestamp", 1000*result.getLong("timestamp"));
					editor.commit();

					String userStr = preferences.getString("fullname", "");
					Toast.makeText(this.context, "Bonjour " + userStr, Toast.LENGTH_SHORT).show();

					// On ouvre une nouvelle Activity -> Onglets
					Intent monIntent = new Intent(this.context, TabsActivity.class);
					startActivity(monIntent);	
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - LoginActivity", e.getMessage());
			}

		}
	}
}

