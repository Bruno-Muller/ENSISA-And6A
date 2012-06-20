package projet1A.And6a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		// On teste la conexion
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((conMan.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED) || (conMan.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)) {
			Log.d("And6@ - SplashScreenActivity", "INTERNET - oui");
			new Downloader().execute();
		}
		else {
			Log.d("And6@ - SplashScreenActivity", "INTERNET - non");
			Toast.makeText(getApplicationContext(), "Vous n'avez pas d'accès à internet.", Toast.LENGTH_LONG).show();
		}
	}

	public void setText(String text) {
		((TextView)findViewById(R.id.news)).setText(text);
	}
	
	public void startTimer() {
		Log.d("And6@ - SplashScreenActivity" , " -- Timer --");
		new Timer().schedule(new Task(this), 3000); //Un timer qui s'executera en one shot après 3000 ms
	}

	private class Task extends TimerTask {

		private Context context;

		Task(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			Intent intent = new Intent(this.context, LoginActivity.class);
			startActivity(intent);
			((Activity) this.context).finish();
		}

	}

	private class Downloader extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... params) {
			String text = null;

			try {
				// Texte
				Log.d("And6@ - SplashScreenActivity", " -- Download texte --");
				URL urlTexte = new URL("http://api.trombi.iariss.fr/edito/get");
				HttpURLConnection connection = (HttpURLConnection) urlTexte.openConnection();
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuffer stringBuffer = new StringBuffer();
				String ligne;
				while((ligne = bufferedReader.readLine()) != null) {
					stringBuffer.append(ligne);
					if(!bufferedReader.ready())
						break;
				}
				text = stringBuffer.toString();

			} catch (IOException e) {
				//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - SplashScreenActivity", e.getMessage());
			}

			return text;
		}

		@Override
		protected void onPostExecute(String text) {
			setText(text);
			startTimer();
		}
	}
}