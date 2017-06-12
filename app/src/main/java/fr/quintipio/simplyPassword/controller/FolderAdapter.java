package fr.quintipio.simplyPassword.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.model.Dossier;

import java.util.List;

public class FolderAdapter extends ArrayAdapter<Dossier> {

    public FolderAdapter(Context context, List<Dossier> dossiers) {
        super(context, 0, dossiers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_folder,parent, false);
        }

        FolderViewHolder viewHolder = (FolderViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FolderViewHolder();
            viewHolder.nomDossier = convertView.findViewById(R.id.nomDossier);
            convertView.setTag(viewHolder);
        }

        Dossier dossier = getItem(position);

        viewHolder.nomDossier.setText(dossier.getTitre());

        return convertView;
    }

    private class FolderViewHolder{
        public TextView nomDossier;
    }
}
