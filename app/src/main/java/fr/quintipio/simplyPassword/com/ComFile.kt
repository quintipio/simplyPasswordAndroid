package fr.quintipio.simplyPassword.com


import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Classe pour gérer un fichier
 */
class ComFile
/**
 * Constructeur du fichier
 * @param path le chemin du fichier
 */
(path: String) {

    /**
     * Le fichier utilisé
     */
    /**
     * Getter du fichier
     * @return le fichier
     */
    val file: File

    init {
        this.file = File(path)
    }

    /**
     * Ecrit un String dans un fichier
     * @param data les donénes à écrire
     * @return true si ok
     */
    fun writeString(data: String): Boolean {
        try {
            val stream = FileOutputStream(file)
            stream.write(data.toByteArray())
            stream.close()
            return true
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * Ecrit un byte[] dans un fichier
     * @param data
     * @return true si ok
     */
    fun writeBytes(data: ByteArray): Boolean {
        try {
            val stream = FileOutputStream(file)
            stream.write(data)
            stream.close()
            return true
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * Lit une chaine de caractère dans un fichier
     * @return la chaine de caractère
     */
    fun readString(): String? {
        try {
            val length = file.length().toInt()

            val bytes = ByteArray(length)

            val `in` = FileInputStream(file)
            try {
                `in`.read(bytes)
            } finally {
                `in`.close()
            }

            return String(bytes)
        } catch (e: Exception) {
            return null
        }

    }

    /**
     * Lit un byte[] dans un fichier
     * @return la chaine de caractère
     */
    fun readBytes(): ByteArray? {
        try {
            val length = file.length().toInt()
            val bytes = ByteArray(length)
            val `in` = FileInputStream(file)
            try {
                `in`.read(bytes)
            } finally {
                `in`.close()
            }
            return bytes
        } catch (e: Exception) {
            return null
        }

    }
}
