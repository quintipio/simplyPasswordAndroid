package fr.quintipio.simplyPassword.contexte

object ContexteAppli {

    /**
     * Nom de l'application
     */
    val nomAppli = "Simply Password"

    /**
     * Numéro de version
     */
    val version = "1.1"

    /**
     * Nom du développeur
     */
    val developpeur = "Quentin Delfour"

    /**
     * Chemin du répertoire ou se trouve les textes de l'application
     */
    val bundle = "bundle/strings"

    /**
     * Extension accepté pour charger et sauvegarder les données
     */
    val extension = ".spj"

    /**
     * Extension pour les fichiers de partage de mot de passe
     */
    val extensionPartage = ".spp"

    /**
     * Extension pour les fichiers exporter chiffré
     */
    val extensionExport = ".spe"

    /**
     * Duràe en seconde de la copie d'un identifiant ou d'un mot de passe dans le presse papier
     */
    val dureeTimerCopieClipboard = 20

    /**
     * La liste des langues disponibles
     */
    val listeLangues = arrayOf("fr", "en")

    /**
     * La langue par défaut de l'application
     */
    var langueDefaut = listeLangues[0]

    val sharedPrefKey = "SIMPLY_PASSWORD_SHARE_PREF"

    val filePrefKey = "FILE_PATH"

}
