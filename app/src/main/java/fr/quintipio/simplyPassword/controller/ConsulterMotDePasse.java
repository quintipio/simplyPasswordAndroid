package fr.quintipio.simplyPassword.controller;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.quintipio.simplyPassword.R;
import fr.quintipio.simplyPassword.model.MotDePasse;

public class ConsulterMotDePasse extends AppCompatActivity {

    private MotDePasse motDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_mot_de_passe);
        motDePasse = (MotDePasse)getIntent().getSerializableExtra("mdp");

        Button copyLoginButton = (Button) findViewById(R.id.copyLoginButton);
        Button copyMdpButton = (Button) findViewById(R.id.copyMdpButton);
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

        TextView titre = (TextView) findViewById(R.id.titreTextView);
        TextView login = (TextView) findViewById(R.id.titreTextView);
        TextView password = (TextView) findViewById(R.id.titreTextView);
        TextView site = (TextView) findViewById(R.id.titreTextView);
        TextView commentaire = (TextView) findViewById(R.id.titreTextView);

        titre.setText(motDePasse.getTitre());
        login.setText(motDePasse.getLogin());
        password.setText(motDePasse.getMotDePasseObjet());
        site.setText(motDePasse.getSiteWeb());
        commentaire.setText(motDePasse.getCommentaire());
    }

    public void copyLoginToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("login", motDePasse.getLogin());
        clipboard.setPrimaryClip(clip);
    }

    public void copyPasswordToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("mdp", motDePasse.getMotDePasseObjet());
        clipboard.setPrimaryClip(clip);
    }
}
