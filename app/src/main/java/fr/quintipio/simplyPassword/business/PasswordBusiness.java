package fr.quintipio.simplyPassword.business;

import fr.quintipio.simplyPassword.com.ComFile;
import fr.quintipio.simplyPassword.model.Dossier;
import fr.quintipio.simplyPassword.model.MotDePasse;
import fr.quintipio.simplyPassword.util.CryptUtils;
import fr.quintipio.simplyPassword.util.ObjectUtils;
import fr.quintipio.simplyPassword.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Lance une recherche de mot de passe sur les titres et les logins dans le dossier et ses sous dossiers
     * @param recherche le texte à rechercher
     * @param dossier le dossier dans lequel effectuer la recherche
     * @return les résultats
     */
    public static List<MotDePasse> recherche(String recherche, Dossier dossier) {
        List<MotDePasse> retour = new ArrayList<>();
        if(dossier.getListeMotDePasse() != null && dossier.getListeMotDePasse().size() > 0) {
            for (MotDePasse mdp : dossier.getListeMotDePasse()) {
                if(mdp.getLogin().toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.getCommentaire().toLowerCase().contains(recherche.toLowerCase()) ||
                        mdp.getTitre().toLowerCase().contains(recherche.toLowerCase())) {
                    retour.add(mdp);
                }
            }
        }
        if(dossier.getSousDossier() != null && dossier.getSousDossier().size() > 0) {
            for (Dossier dos : dossier.getSousDossier()) {
                retour.addAll(recherche(recherche, dos));
            }
        }
        return retour;
    }

    public static Dossier getDossierMere() {
        return dossierMere;
    }

}
