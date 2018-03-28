package fr.quintipio.simplyPassword.util

object StringUtils {

    /**
     * Vérifie si une chaine de caractère est vide
     * @param chaine la chaine
     * @return true si vide
     */
    fun stringEmpty(chaine: String?): Boolean {
        return if (chaine == null) {
            true
        } else {
            chaine.trim { it <= ' ' }.isEmpty() || chaine.trim { it <= ' ' }.isEmpty()
        }
    }


}
