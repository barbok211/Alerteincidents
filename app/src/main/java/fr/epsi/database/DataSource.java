package fr.epsi.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import classes.metier.IncidentDB;

public class DataSource {
	private SQLiteDatabase database;
	private DbHelper mDbHelper;

	private String[] allColumns_Incident = { DbHelper.COLUMN_INCIDENT_ID,
			DbHelper.COLUMN_INCIDENT_DATE, DbHelper.COLUMN_INCIDENT_IMEI,
			DbHelper.COLUMN_INCIDENT_NAME, DbHelper.COLUMN_INCIDENT_DESCRIPTION,
			DbHelper.COLUMN_INCIDENT_LONGITUDE, DbHelper.COLUMN_INCIDENT_LATITUDE
			//a rajouter par la suite DbHelper.COLUMN_TYPEINCIDENT_ID
	};

	public DataSource(Context context) {
		mDbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		database = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}
	
	/*Incidents DAO*/
	public IncidentDB createIncident(IncidentDB item){
		
		ContentValues values = new ContentValues();
		if ((Integer) item.get(DbHelper.COLUMN_INCIDENT_ID) != -1)
			values.put(DbHelper.COLUMN_INCIDENT_ID,(Integer) item.get(DbHelper.COLUMN_INCIDENT_ID));

		for (int i = 0; i < allColumns_Incident.length; i++) {
			if (item.contains(allColumns_Incident[i])) {
				values.put(allColumns_Incident[i],
						(String) item.get(allColumns_Incident[i]));
			}
		}
		long insertId = database.insert(DbHelper.TABLE_INCIDENT, null, values);

		Cursor cursor = database.query(DbHelper.TABLE_INCIDENT, allColumns_Incident,
				DbHelper.COLUMN_INCIDENT_ID + " = " + insertId, null, null, null,
				null);

		cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_INCIDENT
				+ " DESC;", null);
		cursor.moveToLast();
        IncidentDB newIncident = cursorToIncident(cursor);
		cursor.close();

		return newIncident;
	}

	public void deleteIncident(IncidentDB Incident) {
		long id = Incident.getId();
		database.delete(DbHelper.TABLE_INCIDENT, DbHelper.COLUMN_INCIDENT_ID + " = "
				+ id, null);
	}

	public List<IncidentDB> getAllIncident() {
		List<IncidentDB> INCIDENT = new ArrayList<IncidentDB>();

		Cursor cursor = database.query(DbHelper.TABLE_INCIDENT, allColumns_Incident,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
            IncidentDB Incident = cursorToIncident(cursor);
			INCIDENT.add(Incident);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return INCIDENT;
	}

	public boolean updateIncident(IncidentDB item) {
		String sql = "INSERT OR IGNORE INTO  " + DbHelper.TABLE_INCIDENT + " ";
		String columns = "(";
		String sql_values = "VALUES (";
		String update = "UPDATE " + DbHelper.TABLE_INCIDENT + " SET ";
		ContentValues values = new ContentValues();
		boolean first_column = true;
		boolean first_value = true;
		boolean first_update = true;
		columns += DbHelper.COLUMN_INCIDENT_ID + ",";
		sql_values += item.getId() + ",";
		values.put(DbHelper.COLUMN_INCIDENT_ID, item.getId());
		for (int i = 1; i < allColumns_Incident.length; i++) {
			if (item.contains(allColumns_Incident[i])) {
				if (!first_column)
					columns += " , ";
				columns += allColumns_Incident[i];

				if (!first_value)
					sql_values += " , ";
				sql_values += "'" + item.get(allColumns_Incident[i]) + "'";

				if (!first_update)
					update += " , ";
				update += allColumns_Incident[i] + " = " + "'"
						+ item.get(allColumns_Incident[i]) + "'";

				values.put(allColumns_Incident[i],
						"" + item.get(allColumns_Incident[i]));
				first_column = false;
				first_value = false;
				first_update = false;
			}

		}
		if (values.size() < 1)
			return false;
		sql += columns + ") " + sql_values + ")";
		update += " WHERE " + DbHelper.COLUMN_INCIDENT_ID + "=" + item.getId() + "";
		Log.v("Insert SQL", sql);
		Log.v("Update SQL", update);

		database.insertWithOnConflict(DbHelper.TABLE_INCIDENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return true;
	}

	public IncidentDB getIncident(long id) {
		Cursor c = database.query(DbHelper.TABLE_INCIDENT, null,
				DbHelper.COLUMN_INCIDENT_ID + "=" + id, null, null, null, null);
		c.moveToNext();
		IncidentDB result = cursorToIncident(c);
		return result;
	}

	private IncidentDB cursorToIncident(Cursor cursor) {
		IncidentDB mIncident = new IncidentDB();
		mIncident.setId(cursor.getLong(0));
		for (int i = 1; i < allColumns_Incident.length; i++) {
			mIncident.setString(allColumns_Incident[i], cursor.getString(i));
		}
		return mIncident;
	}

	/*EOF Incident DAO*/

}
