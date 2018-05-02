package fr.quintipio.simplyPassword.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable

@Root
class MotDePasse @JvmOverloads constructor (
                  @field:Element(required=false, name = "titre") var titre: String = "",
                  @field:Element(required=false,name = "login") var login: String = "",
                  @field:Element(required=false, name = "motDePasseObjet") var motDePasseObjet: String = "",
                  @field:Element(required=false, name = "commentaire") var commentaire: String = "",
                  @field:Element(required=false, name = "siteWeb") var siteWeb: String = "",
                  @field:Element(required=false, name = "idIcone") var idIcone: Int = 0
) : Serializable  {
    @Transient
    var dossierPossesseur: Dossier? = null

}