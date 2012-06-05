package projet1A.And6a;

import org.json.JSONArray;

import projet1A.And6a.http.HTTPJson;
import android.app.ListActivity;
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
import android.widget.ListView;
import android.widget.Toast;

public class ContactsListActivity extends ListActivity {

	private String classe;
	private String year;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		this.classe = "";
		this.year = "";
	}

	public void onResume() {
		super.onResume();
		
		if (this.needToBeReloaded())
			this.reload();
	}

	private boolean needToBeReloaded() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		if (!this.classe.equals(preferences.getString("class", "")))
			return true;

		if (!this.year.equals(preferences.getString("year", "")))
			return true;

		return false;
	}




	private void reload() {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		this.classe = preferences.getString("class", "");
		this.year = preferences.getString("year", "");

		String url = "http://api.trombi.iariss.fr/" + preferences.getString("salt", "") + "/users/get/"+this.classe + "/" +this.year;
		
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((conMan.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED) || (conMan.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)) {
			Log.d("And6@ - ContactCardActivity", "INTERNET - oui");
			new Downloader(this).execute(url);
		}
		else {
			Log.d("And6@ - ContactCardActivity", "INTERNET - non");
			Toast.makeText(getApplicationContext(), "Vous n'avez pas d'accès à internet.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Contact contact = (Contact) this.getListAdapter().getItem(position);

		// Transmet le paramètre id à l'activité à démarrer
		Intent intent = new Intent(getApplicationContext(),ContactCardActivity.class);
		intent.putExtra("id", contact.getId());
		startActivity(intent);		
	}
	
	private class Downloader extends AsyncTask<String, Void, JSONArray>
	{
		
		private ProgressDialog progressDialog;
		private Context context;

		Downloader(Context context) {
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			this.progressDialog = new ProgressDialog(this.context);
			this.progressDialog.setMessage("Chargement en cours...");
			this.progressDialog.show();
		}

		@Override
		protected JSONArray doInBackground(String... url) {

			JSONArray jsonArray = null;

			try {
				Log.d("And6@ - ContactsListActivity", url[0]);
				jsonArray = HTTPJson.doPost(url[0]).getJSONArray("datas");
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - ContactsListActivity", e.getMessage());
			}
			
			return jsonArray;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			try {
				setListAdapter(new ContactsListAdapter(this.context, result));
			} catch(Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - ContactsListActivity", e.getMessage());
			}
			
			this.progressDialog.dismiss();
		}
	}
}