package fr.epsi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import classes.metier.IncidentDB;

public class DbHelper extends SQLiteOpenHelper{
	
	//list of tables
	public static final String NOM_TABLE_INCIDENT = "Incident";
	//name of db
	public static final String DATABASE_NAME = "DB_AI.db";
	//version
	public static int DATABASE_VERSION = 2;
	// Database creation sql statement

	// Columns Incident Table
	public static final String COLUMN_INCIDENT_ID = "incident_id";
	public static final String COLUMN_INCIDENT_DATE = "incident_date";
	public static final String COLUMN_INCIDENT_TITRE = "incident_name";
	public static final String COLUMN_INCIDENT_LONGITUDE = "incident_longitude";
	public static final String COLUMN_INCIDENT_LATITUDE = "incident_latitude";
	public static final String COLUMN_INCIDENT_TYPE_ID = "incident_type_id";


	//constructeur avec un context
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
				"CREATE TABLE Incident ("
						+ "incident_id INTEGER PRIMARY KEY," + "incident_date TEXT,"
						+ " incident_name TEXT," + "incident_longitude TEXT,"
						+ "incident_latitude TEXT," + " incident_type_id TEXT"
						+ " );"
		);
	}
	
	//upgrade db
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + NOM_TABLE_INCIDENT);
		onCreate(db);
	}

	public boolean insertIncident (String incident_date, String incident_name,
									String incident_longitude,String incident_latitude,
									String type_incident_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("incident_date", incident_date);
		contentValues.put("incident_name", incident_name);
		contentValues.put("incident_longitude", incident_longitude);
		contentValues.put("incident_latitude", incident_latitude);
		contentValues.put("incident_type_id", type_incident_id);
		db.insert("Incident", null, contentValues);
		return true;
	}

	public Cursor getData(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery( "SELECT * FROM "+ NOM_TABLE_INCIDENT +" WHERE id="+id+"", null );
		return res;
	}

	public int numberOfRows(){
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, NOM_TABLE_INCIDENT);
		return numRows;
	}
	public boolean updateIncident (Integer incident_id, String incident_date,String incident_name,
								  String incident_longitude,String incident_latitude,
								  String type_incident_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("incident_date", incident_date);
		contentValues.put("incident_name", incident_name);
		contentValues.put("incident_longitude", incident_longitude);
		contentValues.put("incident_latitude", incident_latitude);
		contentValues.put("incident_type_id", type_incident_id);
		db.update(NOM_TABLE_INCIDENT, contentValues, "id = ? ", new String[] { Integer.toString(incident_id) } );
		return true;
	}

	public Integer deleteIncident (Integer incident_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOM_TABLE_INCIDENT,"id = ? ",new String[] { Integer.toString(incident_id) });
	}

	public ArrayList<IncidentDB> getAllIncidents()
	{
		Log.v("===QUERY","SELECT * FROM "+ NOM_TABLE_INCIDENT);
		ArrayList<IncidentDB> array_list = new ArrayList<IncidentDB>();
		//hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery( "SELECT * FROM "+ NOM_TABLE_INCIDENT, null );
		res.moveToFirst();

		Log.v("===RES", res.toString());


		while(res.isAfterLast() == false){
			IncidentDB incident_item = new IncidentDB();
			String idIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_ID));
			String titreIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_TITRE));
			String dateIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_DATE));
			String latIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_LATITUDE));
			String lngIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_LONGITUDE));
			String idTypeIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_TYPE_ID));

			incident_item.setString(DbHelper.COLUMN_INCIDENT_ID,
					String.valueOf(idIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_TITRE,
					String.valueOf(titreIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_DATE,
					String.valueOf(dateIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_LATITUDE,
					String.valueOf(latIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_LONGITUDE,
					String.valueOf(lngIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_TYPE_ID,
					String.valueOf(idTypeIncident));

			array_list.add(incident_item);
			res.moveToNext();
		}
		Log.v("===RAREY",String.valueOf(array_list.size()));
		return array_list;
	}
}
