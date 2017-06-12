package fr.quintipio.simplyPassword.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.model.MotDePasse;

import java.util.List;

public class MotDePasseAdapter extends ArrayAdapter<MotDePasse> {

    public MotDePasseAdapter(Context context, List<MotDePasse> mdps) {
        super(context, 0, mdps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_mdp,parent, false);
        }

        MotDePasseAdapter.MotDePasseViewHolder viewHolder = (MotDePasseAdapter.MotDePasseViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MotDePasseAdapter.MotDePasseViewHolder();
            viewHolder.titre = convertView.findViewById(R.id.titreMdp);
            viewHolder.login = convertView.findViewById(R.id.loginMdp);
            convertView.setTag(viewHolder);
        }

        MotDePasse motDePasse = getItem(position);

        viewHolder.titre.setText(motDePasse.getTitre());
        viewHolder.login.setText(motDePasse.getLogin());

        return convertView;
    }


    private class MotDePasseViewHolder{
        public TextView titre;
        public TextView login;
    }
}
