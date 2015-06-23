package fr.epsi.alerteincidents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import classes.metier.IncidentDB;
import fr.epsi.database.DataSource;
import fr.epsi.helper.FusedLocationService;

public class CarteActivity extends Activity {

	static final LatLng FRANCE = new LatLng(47, 2);
	private GoogleMap map;
	private FusedLocationService mFusedLocation;
	private CharSequence mTitle;
	private HashMap<Marker,IncidentDB> marker2incident;
	private DataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carte);
		if (!isGooglePlayServicesAvailable()) {
			finish();
		}		
		//change le titre de l'activite
		setTitle("Carte");
		//recupere le map par son id
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);	
		map = mapFragment.getMap();
		//affiche le point de notre position
		map.setMyLocationEnabled(true);
		//zoom sur notre position si gps active, sinon zoom sur France
		mFusedLocation = new FusedLocationService(this);
		Location location = mFusedLocation.getLocation();
		marker2incident = new HashMap<Marker, IncidentDB>();
		dataSource = new DataSource(this);

		try {
			dataSource.open();
		} catch (SQLException e){
			//Log.e("sql error", e.getMessage());
		}
		
		Log.v("addMarkersIncidents","YOEOE");
		addMarkersIncidents(dataSource);

		// est appele lorsqu'on clique sur la fenetre info
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
			@Override
			public void onInfoWindowClick(Marker marker)
			{
                IncidentDB item = marker2incident.get(marker);
				SharedPreferences prefs = getSharedPreferences("AlerteIncidents",Context.MODE_PRIVATE);
				Editor edit = prefs.edit();
				edit.putLong("item_id", item.getId());
				edit.putBoolean("is_golf", false);
				edit.commit();
				startDetailActivity();

			}
		});

		// Move the camera instantly with a zoom .
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(FRANCE, 5));

	}

	public void startDetailActivity(){
		Intent i = new Intent(CarteActivity.this,MainActivity.class);
		startActivity(i);
		this.finish();
	}

	//add Incidents on map with marker
	private void addMarkersIncidents(DataSource data_source)
	{
		List<IncidentDB> list_Incident = data_source.getAllIncident();

		Iterator<IncidentDB> it = list_Incident.iterator();
		int i=0;
		while (it.hasNext())
		{
			/*Log.v("MarkerAdd", String.valueOf(i) + " Times");
			Incident item = (Incident) it.next();
			LatLng latlng = new LatLng(Long.valueOf(item.getString(DbHelper.COLUMN_INCIDENT_LATITUDE)),
					Long.valueOf(item.getString(DbHelper.COLUMN_INCIDENT_LONGITUDE)));
			Marker temp = map.addMarker(new MarkerOptions()
			.position(latlng)
			.title(item.getString(DbHelper.COLUMN_INCIDENT_NAME))
			.snippet(item.getString(DbHelper.COLUMN_INCIDENT_DATE))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
			.visible(true)
					);
			marker2incident.put(temp, item);*/
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}
	//gestion bouton retour
	public void onBackPressed(){
		//recupere le nom de l'activite precedente
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		String current = mPrefs.getString("ActivityName", "NoName");
		Log.v("onBackPressed",current);

		//donne le nom de l'activite
		Editor edit = mPrefs.edit();
		edit.putString("ActivityName", "CarteActivity");
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
		if (current.equals("PreferencesActivity")){
			Intent intent = new Intent(this,PreferencesActivity.class);
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

	//add marker on map
	private void addMarkerIncident(ArrayList<IncidentDB> mList)
	{
		Log.v("addMarkerIncident",String.valueOf(mList.size()));
		List<IncidentDB> list_incident = mList;//liste des incidents
		Iterator<IncidentDB> it = list_incident.iterator();
		/*
		while (it.hasNext())
		{
			Incident item = (Incident) it.next();
			//l'image du marker
			int res = R.drawable.ic_launcher;
			LatLng mLatlng = new LatLng(item.getLatitude(),item.getLongitude());
			String title = item.getTitre();

			Marker temp = map.addMarker(new MarkerOptions()
			//position en LatLng de l'infobulle	
			.position(mLatlng)
			//titre de l'infobulle
			.title(title)
			//description de l'infobulle
			.snippet("Rebel")
			.icon(BitmapDescriptorFactory.fromResource(res))
			.visible(true)
					);

			marker2incident.put(temp, item);
		}*/
	}
}
