package fr.quintipio.simplyPassword.business

import fr.quintipio.simplyPassword.com.ComFile
import fr.quintipio.simplyPassword.model.Dossier
import fr.quintipio.simplyPassword.model.MotDePasse
import fr.quintipio.simplyPassword.util.CryptUtils
import fr.quintipio.simplyPassword.util.ObjectUtils
import fr.quintipio.simplyPassword.util.StringUtils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.ArrayList

/**
 * Classe de gestion des mots de passe
 */
object PasswordBusiness {


    private var fichier: ComFile? = null

    private var motDePasse: String? = null

    var dossierMere: Dossier? = null
        private set


    @Throws(Exception::class)
    fun charger(nouveauMotDePasse: String, path: String) {
        val nouveauFichier = ComFile(path)
        val data = nouveauFichier.readBytes()
        val input = ByteArrayInputStream(data)
        val output = ByteArrayOutputStream()
        CryptUtils.decrypt(nouveauMotDePasse.toCharArray(), input, output)
        dossierMere = ObjectUtils.deserialize(output.toByteArray()) as Dossier
        fichier = nouveauFichier
        motDePasse = nouveauMotDePasse
    }

    /**
     * Lance une recherche de mot de passe sur les titres et les logins dans le dossier et ses sous dossiers
     * @param recherche le texte à rechercher
     * @param dossier le dossier dans lequel effectuer la recherche
     * @return les résultats
     */
    fun recherche(recherche: String, dossier: Dossier): List<MotDePasse> {
        val retour = ArrayList<MotDePasse>()
        if (dossier.listeMotDePasse != null && dossier.listeMotDePasse!!.size > 0) {
            for (mdp in dossier.listeMotDePasse!!) {
                if (mdp.login!!.toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.commentaire!!.toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.titre!!.toLowerCase().contains(recherche.toLowerCase())) {
                    retour.add(mdp)
                }
            }
        }
        if (dossier.sousDossier != null && dossier.sousDossier!!.size > 0) {
            for (dos in dossier.sousDossier!!) {
                retour.addAll(recherche(recherche, dos))
            }
        }
        return retour
    }

}
