package projet1A.And6a;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class ScheduleActivity extends Activity {

	String imgSrc = "ensisa.pinade.org/img/%1/%2/%3/img";

	String configClasse = "";
	String configYear = "";
	int configWeek = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
				
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		long _time = preferences.getLong("timestamp", 0);
		
		this.configWeek = weekBetween(_time);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (cal.get(Calendar.DAY_OF_WEEK) > Calendar.FRIDAY); {
			this.configWeek++;
		}
		
	}
	
	public void onResume() {
		super.onResume();
		
		if (this.needToBeReloaded())
			this.reload();
	}
	
	private boolean needToBeReloaded() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		if (!this.configClasse.equals(preferences.getString("class", "")))
			return true;
		
		if (!this.configYear.equals(preferences.getString("year", "")))
			return true;
		
		return false;
	}
	
	private void reload() {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						
		// MAJ des options
		this.configClasse = preferences.getString("class", "");
		this.configYear = preferences.getString("year", "");
		
		// On charge l'image
		this.loadImage();
	}

	public void imgNextClick(View view)
	{
		this.configWeek++;
		this.loadImage();
	}

	public void imgPrevClick(View view)
	{
		this.configWeek--;
		this.loadImage();
	}

	protected void loadImage()
	{		
		// On transforme l'URL dynamiquement
		String _imgSrc = this.imgSrc;
		_imgSrc = _imgSrc.replaceAll("%1", this.configClasse)
				.replaceAll("%2", String.valueOf(this.configYear))
				.replaceAll("%3", String.valueOf(this.configWeek))
				.replaceAll("//", "/").replaceAll("/0/", "/");

		// On charge l'image
		Log.d("And6@ - ScheduleActivity", "IMG url = " + _imgSrc);
		
		WebView myWebView = (WebView) findViewById(R.id.edt_calendar);
		myWebView.loadUrl("http://" + _imgSrc);
		
		// On change les titres
		TextView textview1 = (TextView) findViewById(R.id.edt_header1);
		textview1.setText(this.configYear.toUpperCase() + " " + this.configClasse);

		TextView textview2 = (TextView) findViewById(R.id.edt_header2);
		textview2.setText("Semaine " + this.configWeek);
		
	}

	static public int weekBetween(long timeOrigin)
	{		
		return (int) ((new Date().getTime()-timeOrigin)/(1000*60*60*24*7));
	}
}