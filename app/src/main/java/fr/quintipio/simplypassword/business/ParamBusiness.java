package fr.quintipio.simplypassword.business;

/**
 * Classe de gestion des paramètres
 */
public class ParamBusiness {
    private static String filePath;

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        ParamBusiness.filePath = filePath;
    }


}
