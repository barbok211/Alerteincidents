package fr.epsi.alerteincidents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class PreferencesActivity extends Activity {
	// used to store app title
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
        //change le titre de l'activite
        setTitle("Préférences");
		
		//recupere les checkboxs par l'id
		CheckBox cb1,cb2,cb3;
		cb1 = (CheckBox) findViewById(R.id.checkBoxAccident);
		cb2 = (CheckBox) findViewById(R.id.checkBoxTravaux);
		cb3 = (CheckBox) findViewById(R.id.checkBoxCatastrophe);
		
	}
	
	//Call when click on buttonEnregistrer
	public void registerPreference(){
		Button btnEnregistrer = (Button) this.findViewById(R.id.buttonEnregistrer);
		
		btnEnregistrer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updatePref();
			}
		});
	}
	
	//Call when click on buttonAnnuler
	public void cancelPreference(){
		Button btnAnnuler = (Button) this.findViewById(R.id.buttonAnnuler);
		
		btnAnnuler.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelUpdate();
			}
		});
		
	}
	
	public void cancelUpdate(){
	}
	
	public void updatePref(){
		
	}
	
	//gestion bouton retour
	public void onBackPressed(){
		//recupere le nom de l'activite precedente
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		String current = mPrefs.getString("ActivityName", "NoName");
		Log.v("onBackPressed",current);
		
        //donne le nom de l'activite
		Editor edit = mPrefs.edit();
		edit.putString("ActivityName", "PreferencesActivity");
		edit.commit();
		
		//test du nom de l'activite
		if (current.equals("MainActivity")){
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("HistoriqueActivity")){
			Intent intent = new Intent(this,HistoriqueActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("IncidentActivity")){
			Intent intent = new Intent(this,IncidentActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("CarteActivity")){
			Intent intent = new Intent(this,CarteActivity.class);
			startActivity(intent);
			this.finish();
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
		
        int id = item.getItemId();
        if (id == R.id.action_main) {
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_carte) {
			Intent intent = new Intent(this,CarteActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_historique) {
			Intent intent = new Intent(this,HistoriqueActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_incident) {
			Intent intent = new Intent(this,IncidentActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_preferences) {
			Intent intent = new Intent(this,PreferencesActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	//set app title on actionBar
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
}
