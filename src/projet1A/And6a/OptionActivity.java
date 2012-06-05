package projet1A.And6a;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends Activity
{
	
	private class Bouton {
		
		private int id;
		private String classe;
		private String year;
		
		Bouton(int id, String classe, String year) {
			this.id = id;
			this.classe = classe;
			this.year = year;
		}
		public int getId() {
			return this.id;
		}
		
		public String getClasse() {
			return this.classe;
		}
		
		public String getYear() {
			return this.year;
		}
	}
	
	ArrayList<Bouton> tab;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);
		
		tab = new ArrayList<Bouton>();
		tab.add(new Bouton(R.id.Info_1A, "info", "1a"));
		tab.add(new Bouton(R.id.Info_2A, "info", "2a"));
		tab.add(new Bouton(R.id.Info_3A, "info", "3a"));
		tab.add(new Bouton(R.id.MR_Info, "info",  "mr"));
		tab.add(new Bouton(R.id.Auto_1A, "auto", "1a"));
		tab.add(new Bouton(R.id.Auto_2A, "auto", "2a"));
		tab.add(new Bouton(R.id.Auto_3a, "auto", "3a"));
		tab.add(new Bouton(R.id.MR_Auto, "auto", "mr"));
		tab.add(new Bouton(R.id.Meca_1A, "meca", "1a"));
		tab.add(new Bouton(R.id.Meca_2A, "meca", "2a"));
		tab.add(new Bouton(R.id.Meca_3A, "meca", "3a"));
		tab.add(new Bouton(R.id.Textile_1A, "text", "1a"));
		tab.add(new Bouton(R.id.Textile_2A, "text", "2a"));
		tab.add(new Bouton(R.id.Textile_3A, "text", "3a"));
		tab.add(new Bouton(R.id.Sys_de_Prod_1A, "prod", "1a"));
		tab.add(new Bouton(R.id.Sys_de_Prod_2A, "prod", "2a"));
		tab.add(new Bouton(R.id.Sys_de_Prod_3A, "prod", "3a"));
		tab.add(new Bouton(R.id.L3_Meca, "lmme", "l3"));
		tab.add(new Bouton(R.id.M1_Meca, "lmme", "m1"));
		tab.add(new Bouton(R.id.M2_Meca,  "lmme", "m2"));
		tab.add(new Bouton(R.id.MSF_Meca, "lmme", "msf"));
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		Iterator<Bouton> iterator = tab.iterator();
		while (iterator.hasNext()) {
			Bouton bouton = iterator.next();
			if (bouton.getClasse().equals(prefs.getString("class", "")) && bouton.getYear().equals(prefs.getString("year", ""))) {
				((Button)findViewById(bouton.getId())).setEnabled(false);
				break;
			}
		}
	}
		
	public void onClick(View v) {
				
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = prefs.edit();
		
		Iterator<Bouton> iterator = tab.iterator();
		while (iterator.hasNext()) {
			Bouton bouton = iterator.next();
			Button button = (Button)findViewById(bouton.getId());
			if (bouton.getId() == v.getId()) {
				editor.putString("class", bouton.getClasse());
				editor.putString("year", bouton.getYear());
				button.setEnabled(false);
				
			}
			else
				button.setEnabled(true);
			button.invalidate();	
		}
		
		editor.commit();	
	}
}