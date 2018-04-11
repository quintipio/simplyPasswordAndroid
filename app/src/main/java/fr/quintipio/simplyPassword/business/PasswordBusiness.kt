package fr.quintipio.simplyPassword.business

import fr.quintipio.simplyPassword.com.ComFile
import fr.quintipio.simplyPassword.model.Dossier
import fr.quintipio.simplyPassword.model.MotDePasse
import fr.quintipio.simplyPassword.util.CryptUtils
import java.io.*
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

/**
 * Classe de gestion des mots de passe
 */
object PasswordBusiness {

    var dossierMere: Dossier = Dossier()
        private set

    var fichier: ComFile? = null
        private set

    private var motDePasse: String = ""

    private var modif: Boolean = false

    private val clePartage = "p55sbev7/2>tV8m7^]Fm4#4jJp%),a3fCpxE.E8?Fd{a=yE*g2R/yt(yG6~vu<fK,^5eP9?~EV8\$Bm8kc3L8X9.d7)bT#VH9JAjJ44!t279fR53M?3>rLX8.TmX77Y52)mTT5H7Ac27^mK99R+U@F@3Ac{-45n*r@PkJ4Y3Mg5sw2pr8CC9)95s9]Q4.~g*g2,m4t2_*95AT%C[KK7U;uA>^PgLLdU>}/aij&Luyf&~,3;6TX$&e_Z45;2E^SzyH"


    /**
     * Charge les données à partir d'un fichier
     * @param path le chemin du fichier à charger
     * @param nouveauMotDePasse le mot de passe de déchiffrement
     * @throws Exception
     */
    @Throws(Exception::class)
    fun load(path: String, nouveauMotDePasse: String) {
        val nouveauFichier = ComFile(path)
        val data = nouveauFichier.readBytes()
        val input = ByteArrayInputStream(data)
        val output = ByteArrayOutputStream()
        CryptUtils.decrypt(nouveauMotDePasse.toCharArray(), input, output)

        val xml = String(output.toByteArray(),charset("UTF-8"))
        val context  = JAXBContext.newInstance(Dossier::class.java)
        val marshaller = context.createUnmarshaller()
        dossierMere = marshaller.unmarshal(StringReader(xml)) as Dossier
        construireElementParent(dossierMere,null)
        fichier = nouveauFichier
        motDePasse = nouveauMotDePasse
        modif = false
    }

    /**
     * Sauvegarde dans un fichier les mots de passe et dossiers
     */
    @Throws(Exception::class)
    fun save() {

        val context  = JAXBContext.newInstance(Dossier::class.java)
        val marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        val wr = StringWriter()
        marshaller.marshal(dossierMere,wr)
        val xml = wr.toString()

        val input = ByteArrayInputStream(xml.toByteArray(charset("UTF-8")))
        val output = ByteArrayOutputStream()
        CryptUtils.encrypt(128, motDePasse.toCharArray(), input, output)
        val pathTmp = fichier!!.file.absolutePath
        val fichierNew = ComFile(pathTmp + "_new")
        fichierNew.writeBytes(output.toByteArray())
        fichier!!.file.delete()
        fichierNew.file.renameTo(File(pathTmp))
        modif = false
    }


    /**
     * Vérifie si un mot de passe existe bien
     * @return true si ok
     */
    fun isMotDePasse(): Boolean {
        return !this.motDePasse.isBlank()
    }


    /**
     * Réinitialise les données
     */
    fun reset() {
        dossierMere = Dossier()
        motDePasse = ""
        fichier = null
        modif = false
    }

    /**
     * Lance une recherche de mot de passe sur les titres et les logins dans le dossier et ses sous dossiers
     * @param recherche le texte à rechercher
     * @param dossier le dossier dans lequel effectuer la recherche
     * @return les résultats
     */
    fun recherche(recherche: String, dossier: Dossier): List<MotDePasse> {
        val retour = ArrayList<MotDePasse>()
        if (dossier.listeMotDePasse.isNotEmpty()) {
            for (mdp in dossier.listeMotDePasse) {
                if (mdp.login.toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.commentaire.toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.titre.toLowerCase().contains(recherche.toLowerCase())) {
                    retour.add(mdp)
                }
            }
        }
        if (dossier.sousDossier.isNotEmpty()) {
            for (dos in dossier.sousDossier) {
                retour.addAll(recherche(recherche, dos))
            }
        }
        return retour
    }

    /**
     * Refait la hiérarchie des dossiers parents, mots de passe...
     * @param dossier le dossier à scanner
     * @param dossierParent le dossier parent à inscrire dans le dossier
     * @return le dossier
     */
    private fun construireElementParent(dossier: Dossier, dossierParent: Dossier?): Dossier {
        dossier.dossierParent = dossierParent

        if (dossier.listeMotDePasse.isNotEmpty()) {
            dossier.listeMotDePasse.forEach { motDePasse -> motDePasse.dossierPossesseur = dossier }
        }


        if (dossier.sousDossier.isNotEmpty()) {
            dossier.sousDossier.forEach { dossier1 -> construireElementParent(dossier1, dossier) }
        }
        return dossier
    }

}
