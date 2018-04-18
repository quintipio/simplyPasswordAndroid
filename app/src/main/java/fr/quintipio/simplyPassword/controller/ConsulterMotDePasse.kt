package fr.quintipio.simplyPassword.controller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.model.MotDePasse
import kotlinx.android.synthetic.main.activity_consulter_mot_de_passe.*

class ConsulterMotDePasse : AppCompatActivity() {

    private var motDePasse: MotDePasse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter_mot_de_passe)
        motDePasse = intent.getSerializableExtra("mdp") as MotDePasse
        afficherMotDePasseCheckBox.setOnCheckedChangeListener { _, _ -> afficherMotdePasse() }
        copyLoginButton.setOnClickListener { copyLoginToClipBoard() }
        copyMdpButton.setOnClickListener { copyPasswordToClipBoard() }


        consulterMdpTitreTextView.text = motDePasse?.titre
        consulterMdpLoginTextView.text = motDePasse?.login
        afficherMotdePasse()

        if (motDePasse?.siteWeb?.isNotBlank() == true) {
            consulterMdpSiteTextView.visibility = View.INVISIBLE
            textWebTextView.visibility = View.INVISIBLE
        } else {
            consulterMdpSiteTextView.text = motDePasse!!.siteWeb
        }
        if (motDePasse?.commentaire?.isNotBlank() == true) {
            consulterMdpCommentaireTextView.visibility = View.INVISIBLE
            textCommentaireTextView.visibility = View.INVISIBLE
        } else {
            consulterMdpCommentaireTextView.text = motDePasse!!.commentaire
        }
    }

    /**
     * Copie un login dans le presse papier
     */
    fun copyLoginToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", motDePasse?.login ?:"")
        clipboard.primaryClip = clip
    }

    /**
     * Copie un mot de passe dans le presse papier
     */
    fun copyPasswordToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", motDePasse?.motDePasseObjet ?:"")
        clipboard.primaryClip = clip
    }

    /**
     * Affiche le mot de passe ou des étoiles en fonctione de la confidentialité choisie
     */
    fun afficherMotdePasse() {
        consulterMdpMdpTextView.text = if (afficherMotDePasseCheckBox.isChecked) motDePasse!!.motDePasseObjet else "********"
    }
}
