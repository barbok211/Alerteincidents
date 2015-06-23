package fr.epsi.alerteincidents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import classes.metier.TypeIncident;
import fr.epsi.alerteincidents.R;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.TypeIncidentViewHolder>{

    List<TypeIncident> types;

    PreferencesAdapter(List<TypeIncident> t){
        this.types = t;
    }

    @Override
    public TypeIncidentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(TypeIncidentViewHolder typeIncidentViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class TypeIncidentViewHolder extends RecyclerView.ViewHolder {
        TextView nomTypeIncident;
        CheckBox checkBoxTypeIncident;

        TypeIncidentViewHolder(View itemView) {
            super(itemView);
            nomTypeIncident = (TextView)itemView.findViewById(R.id.TypesIncidents_nomType);
            checkBoxTypeIncident = (CheckBox)itemView.findViewById(R.id.TypesIncidents_boxType);
        }
    }

}