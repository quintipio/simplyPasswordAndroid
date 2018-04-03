package fr.quintipio.simplyPassword.model


import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlTransient

@XmlRootElement
data class Dossier(var titre: String = "",
                   var listeMotDePasse: MutableList<MotDePasse> = mutableListOf(),
                   var sousDossier: MutableList<Dossier> = mutableListOf(),
                   var idIcone: Int = 0) {

    @XmlTransient
    var dossierParent: Dossier? = null

}