package fr.quintipio.simplyPassword.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.business.PasswordBusiness;
import fr.quintipio.simplyPassword.model.Dossier;
import fr.quintipio.simplyPassword.model.MotDePasse;
import fr.quintipio.simplyPassword.util.StringUtils;

import java.util.List;

public class ListeMotDePasse extends AppCompatActivity {

    private Dossier selectedDossier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_mot_de_passe);

        ImageButton dossierparentButton = (ImageButton) findViewById(R.id.parentFolderImageButton);
        dossierparentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDossier != null && selectedDossier.getDossierParent() != null) {
                    chargerDossier(selectedDossier.getDossierParent(),true);
                }
            }
        });

        EditText rechercheTextBox = (EditText) findViewById(R.id.searchEditText);
        rechercheTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = editable.toString();
                if(!StringUtils.stringEmpty(search)) {
                    List<MotDePasse> result = PasswordBusiness.recherche(search,PasswordBusiness.getDossierMere());
                    chargerRecherche(result);
                }else {
                    chargerDossier(selectedDossier,true);
                }
            }
        });
        chargerDossier(PasswordBusiness.getDossierMere(),true);
    }

    /**
     * Affiche les résultats d'une recherche
     * @param listeResultat la liste des résultats de mots de passe
     */
    public void chargerRecherche(List<MotDePasse> listeResultat) {
        Dossier dossierTmp = new Dossier();
        dossierTmp.setTitre("Recherche"); //TODO internationaliser
        dossierTmp.setListeMotDePasse(listeResultat);
        chargerDossier(dossierTmp,false);
    }

    /**
     * Charge la listeView des dossiers en cours dans la dossier
     * @param dossier le dossier à ouvrir
     * @param changeSelectedDossier indique si oui ou non il faut changer le dossier sélectionné
     */
    public void chargerDossier(Dossier dossier,boolean changeSelectedDossier) {
        TextView titreDossier = (TextView) findViewById(R.id.folderTextView);
        titreDossier.setText(dossier.getTitre());

        ImageButton dossierParent = (ImageButton) findViewById(R.id.parentFolderImageButton);
        dossierParent.setVisibility(dossier.getDossierParent() != null? View.VISIBLE: View.INVISIBLE);

        //Chargement de la liste des dossiers
        ListView dossierList = (ListView) findViewById(R.id.folderListView);
        FolderAdapter adapter = new FolderAdapter(ListeMotDePasse.this, dossier.getSousDossier());
        dossierList.setAdapter(adapter);

        dossierList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dossier item =(Dossier) adapterView.getItemAtPosition(i);
                chargerDossier(item,true);
            }
        });


        //Chargement de la liste des mots de passe
        ListView mdpList = (ListView) findViewById(R.id.mdpListView);
        MotDePasseAdapter mdpAdapter = new MotDePasseAdapter(ListeMotDePasse.this, dossier.getListeMotDePasse());
        mdpList.setAdapter(mdpAdapter);
        mdpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MotDePasse item =(MotDePasse) adapterView.getItemAtPosition(i);
                openMotDePasse(item);
            }
        });
        if(changeSelectedDossier) {
            selectedDossier = dossier;
        }
    }

    /**
     * Ouvre l'activité d'un mot de passe
     * @param mdp le mot de passe à ouvrir
     */
    public void openMotDePasse(MotDePasse mdp) {
        Intent consulterMdp = new Intent(this, ConsulterMotDePasse.class);
        consulterMdp.putExtra("mdp",mdp);
        startActivity(consulterMdp);
    }
}
