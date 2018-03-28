package fr.quintipio.simplyPassword.controller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.model.MotDePasse
import fr.quintipio.simplyPassword.util.StringUtils

class ConsulterMotDePasse : AppCompatActivity() {

    private var motDePasse: MotDePasse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter_mot_de_passe)
        motDePasse = intent.getSerializableExtra("mdp") as MotDePasse

        val copyLoginButton = findViewById(R.id.copyLoginButton) as Button
        val copyMdpButton = findViewById(R.id.copyMdpButton) as Button
        val box = findViewById(R.id.afficherMotDePasseCheckBox) as CheckBox
        box.setOnCheckedChangeListener { compoundButton, b -> afficherMotdePasse() }
        copyLoginButton.setOnClickListener { copyLoginToClipBoard() }
        copyMdpButton.setOnClickListener { copyPasswordToClipBoard() }

        val titre = findViewById(R.id.consulterMdpTitreTextView) as TextView
        val login = findViewById(R.id.consulterMdpLoginTextView) as TextView
        val site = findViewById(R.id.consulterMdpSiteTextView) as TextView
        val siteText = findViewById(R.id.textWebTextView) as TextView
        val commentaire = findViewById(R.id.consulterMdpCommentaireTextView) as TextView
        val commentaireText = findViewById(R.id.textCommentaireTextView) as TextView

        titre.text = motDePasse!!.titre
        login.text = motDePasse!!.login
        afficherMotdePasse()

        if (StringUtils.stringEmpty(motDePasse!!.siteWeb)) {
            site.visibility = View.INVISIBLE
            siteText.visibility = View.INVISIBLE
        } else {
            site.text = motDePasse!!.siteWeb
        }
        if (StringUtils.stringEmpty(motDePasse!!.commentaire)) {
            commentaire.visibility = View.INVISIBLE
            commentaireText.visibility = View.INVISIBLE
        } else {
            commentaire.text = motDePasse!!.commentaire
        }
    }

    /**
     * Copie un login dans le presse papier
     */
    fun copyLoginToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", motDePasse!!.login)
        clipboard.primaryClip = clip
    }

    /**
     * Copie un mot de passe dans le presse papier
     */
    fun copyPasswordToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", motDePasse!!.motDePasseObjet)
        clipboard.primaryClip = clip
    }

    /**
     * Affiche le mot de passe ou des étoiles en fonctione de la confidentialité choisie
     */
    fun afficherMotdePasse() {
        val password = findViewById(R.id.consulterMdpMdpTextView) as TextView
        val box = findViewById(R.id.afficherMotDePasseCheckBox) as CheckBox
        password.text = if (box.isChecked) motDePasse!!.motDePasseObjet else "********"
    }
}
