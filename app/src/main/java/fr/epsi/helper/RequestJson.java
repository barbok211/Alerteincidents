package fr.epsi.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.SQLException;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import classes.metier.IncidentDB;
import fr.epsi.alerteincidents.AppController;
import fr.epsi.database.DataSource;
import fr.epsi.database.DbHelper;

public class RequestJson {
	private ArrayList<IncidentDB> mListIncident;
	private int mProgress;
	private final DataSource data_source;
	private mCallback callback;

	private static int NONE = 0;
	private static int INCIDENT = 1;
	private static int FINISH = 2;

	private int load_state;

	public interface mCallback {
		void onProgress(int progress);
		void onFinish();
	}

	public RequestJson(Context c){
		this.mProgress = 0;
		this.data_source = new DataSource(c);
		callback = null;
		mListIncident = new ArrayList<IncidentDB>();
		load_state = NONE;

		try {
			this.data_source.open();
		} catch (SQLException e){

		}
	}
	
	public void waitProgress()
	{
		if (mProgress > 99)
		{
			callback.onFinish();
			return;
		}
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	waitProgress();
            }
        }, 1000);
	}
	
	public void loadData(mCallback progressFunc)
	{	
		if (callback == null && progressFunc != null)
			callback = progressFunc;

    	Log.v("initDB","load_state "+load_state);
		if (mProgress == 0 && load_state == NONE) 
		{
			//300 km
			mListIncident = getIncidents();
			load_state = INCIDENT;
			loadData(null);
		}
		else if (mProgress == 0 && load_state == INCIDENT)
		{
			load_state = FINISH;
			data_source.close();
			callback.onFinish();
			mProgress = 100;
			return;
		}
		if (mProgress != 100)
		{
			new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	Log.v("Waiting","still wait");
	            	loadData(null);
	            }
	        }, 1000);
		}
		
		return;
	}

	public ArrayList<IncidentDB> getIncidents(){
		//France
		LatLng latlng = new LatLng(47, 2);
		int rayon = 300;
		Log.v("RequestJson","getIncidentsByLatLng");
		Webservice mWbs = new Webservice();
		mWbs.getIncidentByLatLng(latlng, rayon);

		Log.v("Url Done", mWbs.getUrl());

		StringRequest strReq = new StringRequest(Method.GET,
				mWbs.getUrl(), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.v("onResponse","OK");
				try {
					JSONObject mainObject = new JSONObject(response.toString());
					Log.v("mainObject",String.valueOf(mainObject));
					/*JSONObject myObj = mainObject.getJSONObject("json");
					JSONArray list_incidents = myObj.getJSONArray("json");*/
					JSONArray list_incidents = mainObject.getJSONArray("json");
					
					for(int i=0; i<list_incidents.length(); i++){
                        IncidentDB incident_item = new IncidentDB();
						
						//recuperation donnees
						int idIncident = list_incidents.getJSONObject(i).getInt("idIncident");
						String titreIncident = list_incidents.getJSONObject(i).getString("titreIncident");
						String dateIncident = list_incidents.getJSONObject(i).getString("dateIncident");
						Double latIncident = list_incidents.getJSONObject(i).getDouble("latitude");
						Double lngIncident = list_incidents.getJSONObject(i).getDouble("longitude");
						
						//insertion dans la db
						incident_item.setString(DbHelper.COLUMN_INCIDENT_ID, 
								String.valueOf(idIncident));
						incident_item.setString(DbHelper.COLUMN_INCIDENT_NAME, 
								String.valueOf(titreIncident));
						incident_item.setString(DbHelper.COLUMN_INCIDENT_LATITUDE, 
								String.valueOf(latIncident));
						incident_item.setString(DbHelper.COLUMN_INCIDENT_LONGITUDE,
								String.valueOf(lngIncident));
						
						Log.v(titreIncident, dateIncident +" / "+ String.valueOf(idIncident));
						mListIncident.add(incident_item);
					}
					updateProgress(mProgress+100);
				} catch (JSONException e) {
					Log.d("Incident", "FATAL ERROR");
					e.printStackTrace();
				}                              

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				Log.d("Incident", "Error: " + error.getMessage());
			}
		});

		AppController.getInstance().addToRequestQueue(strReq);
		return mListIncident;
	}
	
	private final void updateProgress(int progress)
	{
		mProgress = progress;
		this.callback.onProgress(mProgress);
		
	}
}

