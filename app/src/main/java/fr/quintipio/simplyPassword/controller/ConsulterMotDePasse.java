package fr.quintipio.simplyPassword.controller;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.model.MotDePasse;
import fr.quintipio.simplyPassword.util.StringUtils;

public class ConsulterMotDePasse extends AppCompatActivity {

    private MotDePasse motDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_mot_de_passe);
        motDePasse = (MotDePasse)getIntent().getSerializableExtra("mdp");

        Button copyLoginButton = (Button) findViewById(R.id.copyLoginButton);
        Button copyMdpButton = (Button) findViewById(R.id.copyMdpButton);
        CheckBox box = (CheckBox) findViewById(R.id.afficherMotDePasseCheckBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                afficherMotdePasse();
            }
        });
        copyLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyLoginToClipBoard();
            }
        });
        copyMdpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyPasswordToClipBoard();
            }
        });

        TextView titre = (TextView) findViewById(R.id.consulterMdpTitreTextView);
        TextView login = (TextView) findViewById(R.id.consulterMdpLoginTextView);
        TextView site = (TextView) findViewById(R.id.consulterMdpSiteTextView);
        TextView siteText = (TextView) findViewById(R.id.textWebTextView);
        TextView commentaire = (TextView) findViewById(R.id.consulterMdpCommentaireTextView);
        TextView commentaireText = (TextView) findViewById(R.id.textCommentaireTextView);

        titre.setText(motDePasse.getTitre());
        login.setText(motDePasse.getLogin());
        afficherMotdePasse();

        if(StringUtils.stringEmpty(motDePasse.getSiteWeb())) {
            site.setVisibility(View.INVISIBLE);
            siteText.setVisibility(View.INVISIBLE);
        }
        else {
            site.setText(motDePasse.getSiteWeb());
        }
        if(StringUtils.stringEmpty(motDePasse.getCommentaire())) {
            commentaire.setVisibility(View.INVISIBLE);
            commentaireText.setVisibility(View.INVISIBLE);
        }
        else {
            commentaire.setText(motDePasse.getCommentaire());
        }
    }

    /**
     * Copie un login dans le presse papier
     */
    public void copyLoginToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", motDePasse.getLogin());
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Copie un mot de passe dans le presse papier
     */
    public void copyPasswordToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", motDePasse.getMotDePasseObjet());
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Affiche le mot de passe ou des étoiles en fonctione de la confidentialité choisie
     */
    public void afficherMotdePasse() {
        TextView password = (TextView) findViewById(R.id.consulterMdpMdpTextView);
        CheckBox box = (CheckBox) findViewById(R.id.afficherMotDePasseCheckBox);
        password.setText((box.isChecked())?motDePasse.getMotDePasseObjet():"********");
    }
}
