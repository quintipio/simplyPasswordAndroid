package fr.quintipio.simplyPassword.model



import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false,name = "dossier")
@Element(name = "dossier")
data class Dossier @JvmOverloads constructor (
                    @field:Element(required = false, name = "titre") var titre: String = "",
                    @field:ElementList(inline=true, required = false, entry = "listeMotDePasse", empty = true) var listeMotDePasse: MutableList<MotDePasse> = mutableListOf(),
                    @field:ElementList(inline=true, required = false, entry = "sousDossier",empty = true) var sousDossier: MutableList<Dossier> = mutableListOf(),
                    @field:Element(required=false,name = "idIcone") var idIcone: Int = 0){

    @Transient
    var dossierParent: Dossier? = null

    override fun toString(): String {
        return titre
    }

}