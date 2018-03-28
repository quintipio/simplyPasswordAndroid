package fr.quintipio.simplyPassword.util

import fr.quintipio.simplyPassword.model.MotDePasse

import java.io.*

/**
 * Classe d'utilitaire liés aux objets en général
 */
object ObjectUtils {

    /**
     * Sérialize un objet
     * @param obj l'objet à sérializer
     * @return le byte[]
     * @throws IOException
     */
    @Throws(IOException::class)
    fun serialize(obj: Any): ByteArray {
        val out = ByteArrayOutputStream()
        val os = ObjectOutputStream(out)
        os.writeObject(obj)
        return out.toByteArray()
    }

    /**
     * Désérialize un objet
     * @param data les données à désérializer
     * @return l'objet à caster
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserialize(data: ByteArray): Any {
        val `in` = ByteArrayInputStream(data)
        val `is` = ObjectInputStream(`in`)
        return `is`.readObject()
    }

    /**
     * Copier un mot de passe
     * @param mdp le mot de passe
     * @return le nouveau mot de passe
     */
    fun copyMotDePasse(mdp: MotDePasse?): MotDePasse {
        val newMdp = MotDePasse()
        if (mdp != null) {
            if (mdp.titre != null) {
                newMdp.titre = mdp.titre
            }

            if (mdp.login != null) {
                newMdp.login = mdp.login
            }

            if (mdp.motDePasseObjet != null) {
                newMdp.motDePasseObjet = mdp.motDePasseObjet
            }
            if (mdp.siteWeb != null) {
                newMdp.siteWeb = mdp.siteWeb!!
            }

            if (mdp.commentaire != null) {
                newMdp.commentaire = mdp.commentaire!!
            }

            if (mdp.idIcone != null) {
                newMdp.idIcone = mdp.idIcone!!
            }
            newMdp.dossierPossesseur = mdp.dossierPossesseur
        }
        return newMdp
    }
}
