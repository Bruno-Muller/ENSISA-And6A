package projet1A.And6a;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import projet1A.And6a.http.HTTPJson;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactCardActivity extends Activity
{

	private Contact contact;
	private ArrayList<MenuCommand> menuCommands;

	public void onCreate(Bundle savedInstanceState)
	{
		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact_card);
		
		this.menuCommands = new ArrayList<MenuCommand>();

		// Récupère le paramètre transmis
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");

		// On teste la conexion
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((conMan.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED) || (conMan.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)) {
			Log.d("And6@ - ContactCardActivity", "INTERNET - oui");
			new Downloader(this).execute(id);
		}
		else {
			Log.d("And6@ - ContactCardActivity", "INTERNET - non");
			Toast.makeText(getApplicationContext(), "Vous n'avez pas d'accès à internet.", Toast.LENGTH_LONG).show();
		}
	}

	public void setContact(Contact contact) {

		this.contact = contact;

		((TextView)findViewById(R.id.name)).setText(contact.getName());
		((TextView)findViewById(R.id.lastName)).setText(contact.getLastName());
		((TextView)findViewById(R.id.promo)).setText(String.valueOf(contact.getPromo()));
		((TextView)findViewById(R.id.classe)).setText(contact.getClasse());
		((TextView)findViewById(R.id.year)).setText(String.valueOf(contact.getYear()));

		Iterator<String> iterator = contact.getAddrsIterator();
		while(iterator.hasNext()) {
			TextView textView = new TextView(this);
			textView.setText("- " + iterator.next().toString());
			((LinearLayout)findViewById(R.id.adresse)).addView(textView);
		}
		
		iterator = contact.getEmailsIterator();
		while(iterator.hasNext()) {
			TextView textView = new TextView(this);
			textView.setText(iterator.next().toString());
			((LinearLayout)findViewById(R.id.email)).addView(textView);
		}
		
		iterator = contact.getTelsIterator();
		int i = 1;
		while(iterator.hasNext()) {
			TextView textView = new TextView(this);
			textView.setText(iterator.next().toString());
			this.menuCommands.add( new MenuCommand(++i, textView.getText().toString(), "sms:"));
			this.menuCommands.add(new MenuCommand(++i, textView.getText().toString(), "tel:"));
			((LinearLayout)findViewById(R.id.telephone)).addView(textView);
		}
	}

	public void setImageBitmap(Bitmap bitmap) {
		((ImageView)findViewById(R.id.contact_icon)).setImageBitmap(bitmap);
	}

	public void onClick(View view) {
		this.setProgressBarIndeterminateVisibility(true);

		Intent viewIntentIariss;

		switch(view.getId()) {
		case R.id.call:
			viewIntentIariss = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + this.contact.getFirstTel()));
			startActivity(viewIntentIariss);
			break;
		case R.id.message:
			viewIntentIariss =new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + this.contact.getFirstTel()));
			startActivity(viewIntentIariss);
			break;
		}
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sousMenu1 = menu.addSubMenu(0, 0, Menu.NONE, "Appel");
		SubMenu sousMenu2 = menu.addSubMenu(0, 1, Menu.NONE, "SMS");
		
		Iterator<MenuCommand> iterator = this.menuCommands.iterator();
		while (iterator.hasNext()) {
			MenuCommand menuCommand = iterator.next();
			if (menuCommand.getCommand().equals("tel:"))
				sousMenu1.add(0, menuCommand.getId(), 0, menuCommand.getNumero());
			else if (menuCommand.getCommand().equals("sms:"))
				sousMenu2.add(0, menuCommand.getId(), 0, menuCommand.getNumero());
		}		
		return true;
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		Iterator<MenuCommand> iterator = this.menuCommands.iterator();
		while (iterator.hasNext()) {
			MenuCommand menuCommand = iterator.next();
			if (menuCommand.getId()== item.getItemId()) {
				Intent  viewIntentIariss =new Intent(Intent.ACTION_VIEW, Uri.parse(menuCommand.getCommand()+menuCommand.getNumero()));
				startActivity(viewIntentIariss);
				break;
			}
		}
		
	
		return true;
	}
	
	private class MenuCommand {
		
		private int id;
		private String numero;
		private String command;
		
		MenuCommand(int id, String numero, String command) {
			this.id = id;
			this.numero = numero;
			this.command = command;
		}
		
		public int getId() {
			return this.id;
		}
		
		public String getNumero() {
			return this.numero;
		}
		
		public String getCommand() {
			return this.command;
		}
	}
		
	private class Result {

		private Contact contact;
		private Bitmap bitmap;

		Result(Contact contact, Bitmap bitmap) {
			this.contact = contact;
			this.bitmap = bitmap;
		}

		public Contact getContact() {
			return this.contact;
		}

		public Bitmap getBitmap() {
			return this.bitmap;
		}
	}

	private class Downloader extends AsyncTask<Integer, Void, Result>
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
		protected Result doInBackground(Integer... id) {

			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

			String urlData = "http://api.trombi.iariss.fr/"+ preferences.getString("salt", "") + "/users/get/" + id[0];
			String urlAvatar = "http://api.trombi.iariss.fr/"+ preferences.getString("salt", "") +"/photo/get/" + id[0];

			Contact contact = null;
			Bitmap bitmap = null;

			try {

				// Données
				Log.d("And6@ - ContactCardActivity", urlData);
				JSONObject jsonObject = HTTPJson.doPost(urlData);
				contact = Contact.createDetailedContact(jsonObject);

				// Image
				Log.d("url photo", urlAvatar);
				URL urlImage = new URL(urlAvatar);
				HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
				InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("And6@ - ContactCardActivity", e.getMessage());
			}

			return new Result(contact, bitmap);
		}

		@Override
		protected void onPostExecute(Result result) {

			setContact(result.getContact());
			setImageBitmap(result.getBitmap());

			this.progressDialog.dismiss();
		}
	}
}