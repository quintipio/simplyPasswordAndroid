package fr.quintipio.simplypassword.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import fr.quintipio.simplypassword.R;
import fr.quintipio.simplypassword.business.ParamBusiness;
import fr.quintipio.simplypassword.business.PasswordBusiness;
import fr.quintipio.simplypassword.util.CryptUtils;

import java.util.ArrayList;

public class ChargerFichier extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_fichier);
        final Button fileButton = (Button) findViewById(R.id.fileButton);
        final Button chargerButton = (Button) findViewById(R.id.ChargerButton);
        final EditText mdpText = (EditText) findViewById(R.id.MotDePasseTextField);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargerFichier();
            }
        });
        chargerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dechiffrer();
            }
        });
        mdpText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                dechiffrer();
                return true;
            }
        });
    }

    /**
     * Lance le file picker pour charger un fichier en mèmoire, en demandant l'autorisation
     */
    private void chargerFichier() {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an app-defined int constant that should be quite unique
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        String[] fileType = {".spj"};
        ArrayList<String> listeFormat = new ArrayList<>();
        listeFormat.add(".spj");
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setActivityTheme(R.style.AppTheme)
                .addFileSupport("SPJ",fileType)
                .pickFile(this);
    }

    /**
     * Charge un fichier dnas l'appli
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && data!=null)
        {
            ArrayList<String> docPaths = new ArrayList<>();
            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

            if(docPaths.get(0).endsWith(".spj")) {
                TextView cheminText = (TextView) findViewById(R.id.fileTextView);
                cheminText.setText(docPaths.get(0));
                findViewById(R.id.MotDePasseFrame).setVisibility(1);
                ParamBusiness.setFilePath(docPaths.get(0));
            }
            else {
                findViewById(R.id.MotDePasseFrame).setVisibility(0);
                ParamBusiness.setFilePath("");
            }
        }
    }

    /**
     * Lance le déchiffrement d'un fichier
     */
    public void dechiffrer() {
        EditText motDePasseTextBox = (EditText) findViewById(R.id.MotDePasseTextField);
        TextView erreur = (TextView) findViewById(R.id.ErreurTextView);
        String mdp = motDePasseTextBox.getText().toString();
        try {
            PasswordBusiness.charger(mdp);
            erreur.setText("");
        }
        catch(CryptUtils.InvalidPasswordException ex) {
            erreur.setText(R.string.erreur_mauvais_mdp);
        }
        catch(Exception e){
            erreur.setText(R.string.erreur_dechiffrement);
        }

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
