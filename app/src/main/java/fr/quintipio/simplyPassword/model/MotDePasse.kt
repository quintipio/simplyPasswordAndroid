package fr.quintipio.simplyPassword.model

import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlTransient

@XmlRootElement
class MotDePasse(var titre: String = "",
                 var login: String? = null,
                 var motDePasseObjet: String? = null,
                 var commentaire: String? = null,
                 var siteWeb: String? = null,
                 var idIcone: Int = 0
){
    @XmlTransient
    var dossierPossesseur: Dossier? = null

}