package projet1A.And6a;

import projet1A.And6a.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MentionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentions);
        
        Button buttonIariss = (Button) findViewById(R.id.buttonIariss);
		buttonIariss.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View arg0) {
				Intent viewIntentIariss = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.iariss.fr/"));
			    startActivity(viewIntentIariss);	 
			}
		});
		
		
		Button buttonUha = (Button) findViewById(R.id.buttonUha);
		buttonUha.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				 Intent viewIntentIariss =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.uha.fr/"));
			     startActivity(viewIntentIariss);				
			}
			
		});
		
		Button buttonEnsisa = (Button) findViewById(R.id.buttonEnsisa);
		buttonEnsisa.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				 Intent viewIntentIariss =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ensisa.uha.fr/"));
			     startActivity(viewIntentIariss);				
			}
			
		});
		
		Button buttonTrombi = (Button) findViewById(R.id.buttonTrombi);
		buttonTrombi.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				 Intent viewIntentIariss =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.trombi.iariss.fr/"));
			     startActivity(viewIntentIariss);				
			}
			
		});
        
	}
} 