package projet1A.And6a;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
				

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec; // reusable tabspec for each tab
		Intent intent;

		// On crée toutes les activity liées aux onglets
		intent = new Intent().setClass(this, ScheduleActivity.class);
		spec = tabHost.newTabSpec("Edt").setIndicator("Edt").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ContactsListActivity.class);
		spec = tabHost.newTabSpec("Annuaire").setIndicator("Annuaire").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MentionActivity.class);
		spec = tabHost.newTabSpec("Mentions").setIndicator("Mentions").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, OptionActivity.class);
		spec = tabHost.newTabSpec("Options").setIndicator("Options").setContent(intent);
		tabHost.addTab(spec);

		// Le premier onglet est actif
		tabHost.setCurrentTab(0);
		
	}
}