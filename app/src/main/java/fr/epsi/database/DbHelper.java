package fr.epsi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	
	//list of tables
	public static final String TABLE_INCIDENT = "Incident";
	//name of db
	public static final String DATABASE_NAME = "DB_AI.db";
	//version
	public static int DATABASE_VERSION = 1;
	// Database creation sql statement
	private static final String CREATE_INCIDENT = "CREATE TABLE Incident ("
			+ "incident_id INTEGER PRIMARY KEY," + "incident_date TEXT,"
			+ "incident_imei TEXT," + " incident_name TEXT,"
			+ "incident_description TEXT," + "incident_longitude TEXT,"
			+ "incident_latitude TEXT," + " type_incident_id INT"
			+ " );";

	// Columns Incident Table
	public static final String COLUMN_INCIDENT_ID = "incident_id";
	public static final String COLUMN_INCIDENT_DATE = "incident_date";
	public static final String COLUMN_INCIDENT_IMEI = "incident_imei";
	public static final String COLUMN_INCIDENT_NAME = "incident_name";
	public static final String COLUMN_INCIDENT_DESCRIPTION = "incident_description";
	public static final String COLUMN_INCIDENT_LONGITUDE = "incident_longitude";
	public static final String COLUMN_INCIDENT_LATITUDE = "incident_latitude";
	
	//Columns Media Table
	//public static final String COLUMN_MEDIA_ID = "media_id";
	
	//constructeur avec un context
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_INCIDENT);
	}
	
	//upgrade db
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENT);
		onCreate(db);
	}
}
