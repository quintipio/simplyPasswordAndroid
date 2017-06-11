package fr.quintipio.simplypassword.business;

import fr.quintipio.simplypassword.com.ComFile;
import fr.quintipio.simplypassword.model.Dossier;
import fr.quintipio.simplypassword.util.CryptUtils;
import fr.quintipio.simplypassword.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Classe de gestion des mots de passe
 */
public class PasswordBusiness {


    private static ComFile fichier;

    private static String motDePasse;

    private static Dossier dossierMere;


    public static void charger(String nouveauMotDePasse,String path) throws Exception {
        ComFile nouveauFichier = new ComFile(path);
        byte[] data = nouveauFichier.readBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CryptUtils.decrypt(nouveauMotDePasse.toCharArray() , input, output);
        dossierMere = (Dossier) ObjectUtils.deserialize(output.toByteArray());
        fichier = nouveauFichier;
        motDePasse = nouveauMotDePasse;
    }
}
