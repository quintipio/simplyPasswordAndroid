package fr.quintipio.simplyPassword.com;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Classe pour gérer un fichier
 */
public class ComFile {

    /**
     * Le fichier utilisé
     */
    private File file;

    /**
     * Getter du fichier
     * @return le fichier
     */
    public File getFile() {
        return file;
    }

    /**
     * Constructeur du fichier
     * @param path le chemin du fichier
     */
    public ComFile(String path) {
        this.file = new File(path);
    }

    /**
     * Ecrit un String dans un fichier
     * @param data les donénes à écrire
     * @return true si ok
     */
    public boolean writeString(String data) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * Ecrit un byte[] dans un fichier
     * @param data
     * @return true si ok
     */
    public boolean writeBytes(byte[] data) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data);
            stream.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * Lit une chaine de caractère dans un fichier
     * @return la chaine de caractère
     */
    public String readString() {
        try {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }

            return new String(bytes);
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * Lit un byte[] dans un fichier
     * @return la chaine de caractère
     */
    public byte[] readBytes() {
        try {
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            return bytes;
        }
        catch(Exception e) {
            return null;
        }
    }
}
