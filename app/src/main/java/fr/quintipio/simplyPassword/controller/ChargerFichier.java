package fr.quintipio.simplyPassword.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.business.PasswordBusiness;
import fr.quintipio.simplyPassword.contexte.ContexteAppli;
import fr.quintipio.simplyPassword.util.CryptUtils;

import java.io.File;
import java.util.ArrayList;

public class ChargerFichier extends AppCompatActivity {

    private String path;

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

        SharedPreferences settings = getSharedPreferences(ContexteAppli.sharedPrefKey, 0);
        path = settings.getString(ContexteAppli.filePrefKey, null);

        if (path != null) {
            if(new File(path).exists()) {
                TextView cheminText = (TextView) findViewById(R.id.fileTextView);
                cheminText.setText(path);
                findViewById(R.id.MotDePasseFrame).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Lance le file picker pour charger un fichier en mèmoire, en demandant l'autorisation
     */
    private void chargerFichier() {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else {
            String[] fileType = {".spj"};
            ArrayList<String> listeFormat = new ArrayList<>();
            listeFormat.add(".spj");
            FilePickerBuilder.getInstance().setMaxCount(10)
                    .setActivityTheme(R.style.AppTheme)
                    .addFileSupport("SPJ",fileType)
                    .pickFile(this);
        }
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
                findViewById(R.id.MotDePasseFrame).setVisibility(View.VISIBLE);
                path = docPaths.get(0);
            }
            else {
                findViewById(R.id.MotDePasseFrame).setVisibility(View.INVISIBLE);
                path = "";
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
            PasswordBusiness.charger(mdp,path);
            erreur.setText("");

            SharedPreferences settings = getSharedPreferences(ContexteAppli.sharedPrefKey, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ContexteAppli.filePrefKey,path);
            editor.commit();

            Intent listeMdp = new Intent(this, ListeMotDePasse.class);
            startActivity(listeMdp);

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
