package fr.quintipio.simplyPassword.model


import java.io.Serializable
import java.util.ArrayList


/**
 * L'objet Dossier
 */
class Dossier : Serializable {

    var titre: String? = null

    var dossierParent: Dossier? = null

    var sousDossier: List<Dossier>? = null

    var listeMotDePasse: List<MotDePasse>? = null

    var idIcone: Int? = null

    constructor() {

    }

    constructor(titre: String, dossierParent: Dossier) {
        this.titre = titre
        this.dossierParent = dossierParent
        sousDossier = ArrayList()
        listeMotDePasse = ArrayList()
        idIcone = 0
    }

    override fun toString(): String {
        return titre.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Dossier) return false

        val dossier = o as Dossier?

        if (if (titre != null) titre != dossier!!.titre else dossier!!.titre != null) return false
        if (if (dossierParent != null) dossierParent != dossier.dossierParent else dossier.dossierParent != null)
            return false
        if (if (sousDossier != null) sousDossier != dossier.sousDossier else dossier.sousDossier != null)
            return false
        if (if (listeMotDePasse != null) listeMotDePasse != dossier.listeMotDePasse else dossier.listeMotDePasse != null)
            return false
        return if (idIcone != null) idIcone == dossier.idIcone else dossier.idIcone == null

    }

    override fun hashCode(): Int {
        var result = if (titre != null) titre!!.hashCode() else 0
        result = 31 * result + if (dossierParent != null) dossierParent!!.hashCode() else 0
        result = 31 * result + if (sousDossier != null) sousDossier!!.hashCode() else 0
        result = 31 * result + if (listeMotDePasse != null) listeMotDePasse!!.hashCode() else 0
        result = 31 * result + if (idIcone != null) idIcone!!.hashCode() else 0
        return result
    }
}
