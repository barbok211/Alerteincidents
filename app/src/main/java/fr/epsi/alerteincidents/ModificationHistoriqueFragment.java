package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import classes.metier.IncidentDB;
import fr.epsi.database.DbHelper;

public class ModificationHistoriqueFragment extends Fragment {
    private View rootView;
    private TextView text_incident_id;
    DbHelper mLocalDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modification_historique, container, false);

        Bundle args = this.getArguments();
        String incident_id = args.getString("incident_id");
        showIncidentDetail(incident_id);
        return rootView;

    }

    private void showIncidentDetail(String incident_id) {
        TextView incident_text_id = (TextView) rootView.findViewById(R.id.histo_incident_text_id);
        EditText incident_text_titre = (EditText) rootView.findViewById(R.id.histo_incident_text_titre);
        EditText incident_text_date = (EditText) rootView.findViewById(R.id.histo_incident_text_date);
        EditText incident_text_type_incident = (EditText) rootView.findViewById(R.id.histo_incident_text_type_incident);

        DbHelper mLocalDatabase = new DbHelper(getActivity().getApplication());
        IncidentDB mLocalIncident = mLocalDatabase.getIncidentData(Integer.valueOf(incident_id));

        incident_text_id.setText(mLocalIncident.getString("incident_id"));
        incident_text_titre.setText(mLocalIncident.getString("incident_titre"));
        incident_text_date.setText(mLocalIncident.getString("incident_date"));
        incident_text_type_incident.setText(mLocalIncident.getString("incident_type_id"));

    }
}
