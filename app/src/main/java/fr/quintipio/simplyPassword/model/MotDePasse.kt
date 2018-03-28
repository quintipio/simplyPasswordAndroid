package fr.quintipio.simplyPassword.model


import java.io.Serializable

/**
 * L'objet Mot de passe
 */
class MotDePasse : Serializable {

    var titre: String? = null

    var login: String? = null

    var motDePasseObjet: String? = null

    var commentaire: String? = null

    var siteWeb: String? = null

    var dossierPossesseur: Dossier? = null

    var idIcone: Int? = null

    override fun toString(): String {
        return titre.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is MotDePasse) return false

        val that = o as MotDePasse?

        if (if (titre != null) titre != that!!.titre else that!!.titre != null) return false
        if (if (login != null) login != that.login else that.login != null) return false
        if (if (motDePasseObjet != null) motDePasseObjet != that.motDePasseObjet else that.motDePasseObjet != null)
            return false
        if (if (commentaire != null) commentaire != that.commentaire else that.commentaire != null)
            return false
        if (if (siteWeb != null) siteWeb != that.siteWeb else that.siteWeb != null) return false
        if (if (dossierPossesseur != null) dossierPossesseur != that.dossierPossesseur else that.dossierPossesseur != null)
            return false
        return if (idIcone != null) idIcone == that.idIcone else that.idIcone == null

    }

    override fun hashCode(): Int {
        var result = if (titre != null) titre!!.hashCode() else 0
        result = 31 * result + if (login != null) login!!.hashCode() else 0
        result = 31 * result + if (motDePasseObjet != null) motDePasseObjet!!.hashCode() else 0
        result = 31 * result + if (commentaire != null) commentaire!!.hashCode() else 0
        result = 31 * result + if (siteWeb != null) siteWeb!!.hashCode() else 0
        result = 31 * result + if (dossierPossesseur != null) dossierPossesseur!!.hashCode() else 0
        result = 31 * result + if (idIcone != null) idIcone!!.hashCode() else 0
        return result
    }
}
