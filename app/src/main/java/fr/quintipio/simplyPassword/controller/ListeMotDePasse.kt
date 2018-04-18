package fr.quintipio.simplyPassword.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.*
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.business.PasswordBusiness
import fr.quintipio.simplyPassword.model.Dossier
import fr.quintipio.simplyPassword.model.MotDePasse
import kotlinx.android.synthetic.main.activity_liste_mot_de_passe.*

import java.util.ArrayList

class ListeMotDePasse : AppCompatActivity() {

    private var selectedDossier: Dossier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_mot_de_passe)

        parentFolderImageButton.setOnClickListener {
            if (selectedDossier?.dossierParent != null) {
                chargerDossier(selectedDossier!!.dossierParent, true)
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val search = editable.toString()
                if (search.isNotBlank()) {
                    val result = PasswordBusiness.recherche(search, PasswordBusiness.dossierMere)
                    chargerRecherche(result)
                } else {
                    chargerDossier(selectedDossier, true)
                }
            }
        })
        chargerDossier(PasswordBusiness.dossierMere, true)
    }

    /**
     * Affiche les résultats d'une recherche
     * @param listeResultat la liste des résultats de mots de passe
     */
    fun chargerRecherche(listeResultat: MutableList<MotDePasse>) {
        val dossierTmp = Dossier()
        dossierTmp.titre = "Recherche" //TODO internationaliser
        dossierTmp.listeMotDePasse = listeResultat
        chargerDossier(dossierTmp, false)
    }

    /**
     * Charge la listeView des dossiers en cours dans la dossier
     * @param dossier le dossier à ouvrir
     * @param changeSelectedDossier indique si oui ou non il faut changer le dossier sélectionné
     */
    fun chargerDossier(dossier: Dossier?, changeSelectedDossier: Boolean) {
        val titreDossier = findViewById(R.id.folderTextView) as TextView
        titreDossier.text = dossier?.titre ?:""

        val dossierParent = findViewById(R.id.parentFolderImageButton) as ImageButton
        dossierParent.visibility = if (dossier?.dossierParent != null) View.VISIBLE else View.INVISIBLE

        val dossierList = findViewById(R.id.folderListView) as ListView
        val mdpList = findViewById(R.id.mdpListView) as ListView

        //Chargement de la liste des dossiers
        val adapter = FolderAdapter(this@ListeMotDePasse, if (dossier?.sousDossier?.isNotEmpty() == true) dossier.sousDossier else ArrayList())
        dossierList.adapter = adapter

        dossierList.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as Dossier
            chargerDossier(item, true)
        }

        //Chargement de la liste des mots de passe
        val mdpAdapter = MotDePasseAdapter(this@ListeMotDePasse, if (dossier?.listeMotDePasse?.isNotEmpty() == true) dossier.listeMotDePasse else ArrayList())
        mdpList.adapter = mdpAdapter
        mdpList.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as MotDePasse
            openMotDePasse(item)
        }
        if (changeSelectedDossier) {
            selectedDossier = dossier
        }
    }

    /**
     * Ouvre l'activité d'un mot de passe
     * @param mdp le mot de passe à ouvrir
     */
    fun openMotDePasse(mdp: MotDePasse) {
        val consulterMdp = Intent(this, ConsulterMotDePasse::class.java)
        consulterMdp.putExtra("mdp", mdp)
        startActivity(consulterMdp)
    }
}
