package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import classes.metier.Incident;
import classes.metier.TypeIncident;
import fr.epsi.helper.FusedLocationService;
import fr.epsi.helper.RestApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class IncidentActivity extends Activity{
    // used to store app title
    private CharSequence mTitle;
    private final String tag = "IncidentActivity";
    private FusedLocationService mFusedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
        //change le titre de l'activite
        setTitle("Ajouter un Incident");

        final Spinner typeIncidentSpinner = (Spinner) findViewById(R.id.typeIncidentSpinner);
        final ArrayAdapter typeIncidentAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.API_URL)
                .build();
        RestApi methods = restAdapter.create(RestApi.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, retrofit.client.Response response) {
                List<TypeIncident> types = (List<TypeIncident>) o;
                typeIncidentAdapter.addAll(types);
                typeIncidentSpinner.setAdapter(typeIncidentAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        methods.getTypesIncidents(callback);

        mFusedLocation = new FusedLocationService(this);

        //bouton valider
        Button validerButton = (Button) findViewById(R.id.validerButton);
        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Location Manager and check for GPS & Network location services
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // Build the alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(IncidentActivity.this);
                    builder.setTitle("Location Services Not Active");
                    builder.setMessage("Please enable Location Services and GPS");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }

                final EditText titreIncident = (EditText)findViewById(R.id.titreIncidentEditText);
                final EditText descIncident = (EditText)findViewById(R.id.descriptionIncidentEditText);

                Location mLastLocation = mFusedLocation.getLocation();

                Incident i = new Incident(Build.SERIAL, titreIncident.getText().toString(), (TypeIncident)typeIncidentSpinner.getSelectedItem(), descIncident.getText().toString(),
                        mLastLocation.getLatitude(), mLastLocation.getLongitude(), java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(MainActivity.API_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setLog(new RestAdapter.Log() {
                            @Override
                            public void log(String msg) {
                                Log.d("Retrofit", msg);
                            }
                        }).build();
                RestApi methods = restAdapter.create(RestApi.class);
                methods.createIncident(i, new Callback<Incident>() {
                    @Override
                    public void success(Incident incident, retrofit.client.Response response) {
                        Toast.makeText(IncidentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(IncidentActivity.this, HistoriqueActivity.class);
                        startActivity(intent);
                        IncidentActivity.this.finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(IncidentActivity.this, "Erreur, vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    //gestion bouton retour
    public void onBackPressed() {
        //recupere le nom de l'activite precedente
        SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
        String current = mPrefs.getString("ActivityName", "NoName");
        Log.v("onBackPressed", current);

        //donne le nom de l'activite
        Editor edit = mPrefs.edit();
        edit.putString("ActivityName", "IncidentActivity");
        edit.commit();

        //test du nom de l'activite
        if (current.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("HistoriqueActivity")) {
            Intent intent = new Intent(this, HistoriqueActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("CarteActivity")) {
            Intent intent = new Intent(this, CarteActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("PreferencesActivity")) {
            Intent intent = new Intent(this, PreferencesActivity.class);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_carte) {
            Intent intent = new Intent(this, CarteActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_historique) {
            Intent intent = new Intent(this, HistoriqueActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_incident) {
            Intent intent = new Intent(this, IncidentActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
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
