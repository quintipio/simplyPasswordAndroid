package fr.quintipio.simplyPassword.util

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import java.security.spec.KeySpec
import java.util.Arrays
import java.util.Random

import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object CryptUtils {

    //POUR AES
    private val CIPHER_SPEC = "AES/CBC/PKCS5Padding"
    private val KEYGEN_SPEC = "PBKDF2WithHmacSHA1"
    private val SALT_LENGTH = 16
    private val AUTH_KEY_LENGTH = 8
    private val ITERATIONS = 32768
    private val BUFFER_SIZE = 1024


    private val listeLettreMinuscule = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
    private val listeLettreMajuscule = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    private val listeChiffre = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    private val listeCaractereSpeciaux = charArrayOf('²', '&', 'é', '"', '#', '\'', '{', '-', '|', 'è', '_', '\\', 'ç', 'à', '@', ')', '(', '[', ']', '=', '+', '}', '£', '$', '¤', '%', 'ù', 'µ', '*', '?', ',', '.', ';', '/', ':', '§', '!', '€', '>', '<')

    /**
     * Calcul la force approximative d'un mot de passe
     * @param motDePasse le mot de passe à calculer
     * @return la force du mot de passe comprise entre 0 et 100
     */
    fun calculerForceMotDePasse(motDePasse: String?): Int {
        var motDePasse = motDePasse
        var somme = 0
        var nbTypePresent = 0
        var minusculePresent = false
        var majusculePresent = false
        var chiffrePresent = false
        var speciauxPresent = false

        if (motDePasse == null) {
            motDePasse = ""
        }

        for (character in motDePasse.toCharArray()) {
            for (c in listeLettreMinuscule) {
                if (c == character) {
                    minusculePresent = true
                    somme += 4
                    break
                }
            }

            for (c in listeLettreMajuscule) {
                if (c == character) {
                    majusculePresent = true
                    somme += 4
                    break
                }
            }

            for (c in listeChiffre) {
                if (c == character) {
                    chiffrePresent = true
                    somme += 2
                    break
                }
            }

            for (c in listeCaractereSpeciaux) {
                if (c == character) {
                    speciauxPresent = true
                    somme += 7
                    break
                }
            }
        }

        if (speciauxPresent) {
            nbTypePresent++
        }
        if (minusculePresent) {
            nbTypePresent++
        }
        if (majusculePresent) {
            nbTypePresent++
        }
        if (chiffrePresent) {
            nbTypePresent++
        }

        when (nbTypePresent) {
            1 -> somme = (somme * 0.75).toInt()
            2 -> somme = (somme * 1.3).toInt()
            3 -> somme = (somme * 1.7).toInt()
            4 -> somme = somme * 2
        }

        if (somme > 100) somme = 100
        return somme
    }


    /**
     * Genere un mot de passe aléatoire composer de caractères majuscules, minuscules, de chiffres et de caractères spéciaux
     * @param longueur longueur du mot de passe souhaité, si 0 sera de 12 caractères
     * @param lettre autorise les lettres minuscules et majuscules dans le mot de passe
     * @param chiffre autorise les chiffres dans le mot de passe
     * @param caracSpeciaux autorise les caractères spéciaux dans le mot de passe
     * @return le mot de passe généré
     */
    fun genereMotdePasse(longueur: Int, lettre: Boolean, chiffre: Boolean, caracSpeciaux: Boolean): String {
        val length = if (longueur == 0) 12 else longueur
        var password = ""
        val rnd = Random()
        for (i in 0 until length) {
            var caracBienCree = false
            do {
                val typeTab = rnd.nextInt(4)
                when (typeTab) {
                    0 -> if (lettre) {
                        password += listeLettreMinuscule[rnd.nextInt(listeLettreMinuscule.size)]
                        caracBienCree = true
                    }
                    1 -> if (lettre) {
                        password += listeLettreMajuscule[rnd.nextInt(listeLettreMajuscule.size)]
                        caracBienCree = true
                    }
                    2 -> if (chiffre) {
                        password += listeChiffre[rnd.nextInt(listeChiffre.size)]
                        caracBienCree = true
                    }
                    3 -> if (caracSpeciaux) {
                        password += listeCaractereSpeciaux[rnd.nextInt(listeCaractereSpeciaux.size)]
                        caracBienCree = true
                    }
                }
            } while (!caracBienCree)
        }
        return password
    }


    //PARTIE AES


    /**
     * @return a new pseudorandom salt of the specified length
     */
    private fun generateSalt(length: Int): ByteArray {
        val r = SecureRandom()
        val salt = ByteArray(length)
        r.nextBytes(salt)
        return salt
    }

    /**
     * Derive an AES encryption key and authentication key from given password and salt,
     * using PBKDF2 key stretching. The authentication key is 64 bits long.
     * @param keyLength
     * length of the AES key in bits (128, 192, or 256)
     * @param password
     * the password from which to derive the keys
     * @param salt
     * the salt from which to derive the keys
     * @return a Keys object containing the two generated keys
     */
    private fun keygen(keyLength: Int, password: CharArray, salt: ByteArray): Keys? {
        val factory: SecretKeyFactory
        try {
            factory = SecretKeyFactory.getInstance(KEYGEN_SPEC)
        } catch (impossible: NoSuchAlgorithmException) {
            return null
        }

        // derive a longer key, then split into AES key and authentication key
        val spec = PBEKeySpec(password, salt, ITERATIONS, keyLength + AUTH_KEY_LENGTH * 8)
        var tmp: SecretKey? = null
        try {
            tmp = factory.generateSecret(spec)
        } catch (impossible: InvalidKeySpecException) {
        }

        val fullKey = tmp!!.encoded
        val authKey = SecretKeySpec( // key for password authentication
                Arrays.copyOfRange(fullKey, 0, AUTH_KEY_LENGTH), "AES")
        val encKey = SecretKeySpec( // key for AES encryption
                Arrays.copyOfRange(fullKey, AUTH_KEY_LENGTH, fullKey.size), "AES")
        return Keys(encKey, authKey)
    }

    /**
     * Encrypts a stream of data. The encrypted stream consists of a header
     * followed by the raw AES data. The header is broken down as follows:<br></br>
     *
     *  * **keyLength**: AES key length in bytes (valid for 16, 24, 32) (1 byte)
     *  * **salt**: pseudorandom salt used to derive keys from password (16 bytes)
     *  * **authentication key** (derived from password and salt, used to
     * check validity of password upon decryption) (8 bytes)
     *  * **IV**: pseudorandom AES initialization vector (16 bytes)
     *
     *
     * @param keyLength
     * key length to use for AES encryption (must be 128, 192, or 256)
     * @param password
     * password to use for encryption
     * @param input
     * an arbitrary byte stream to encrypt
     * @param output
     * stream to which encrypted data will be written
     * @throws InvalidKeyLengthException
     * if keyLength is not 128, 192, or 256
     * @throws StrongEncryptionNotAvailableException
     * if keyLength is 192 or 256, but the Java runtime's jurisdiction
     * policy files do not allow 192- or 256-bit encryption
     * @throws IOException
     */
    @Throws(CryptUtils.InvalidKeyLengthException::class, CryptUtils.StrongEncryptionNotAvailableException::class, IOException::class)
    fun encrypt(keyLength: Int, password: CharArray, input: InputStream, output: OutputStream) {
        // Check validity of key length
        if (keyLength != 128 && keyLength != 192 && keyLength != 256) {
            throw InvalidKeyLengthException(keyLength)
        }

        // generate salt and derive keys for authentication and encryption
        val salt = generateSalt(SALT_LENGTH)
        val keys = keygen(keyLength, password, salt)

        // initialize AES encryption
        var encrypt: Cipher? = null
        try {
            encrypt = Cipher.getInstance(CIPHER_SPEC)
            encrypt!!.init(Cipher.ENCRYPT_MODE, keys!!.encryption)
        } catch (impossible: NoSuchAlgorithmException) {
        } catch (impossible: NoSuchPaddingException) {
        } catch (e: InvalidKeyException) { // 192 or 256-bit AES not available
            throw StrongEncryptionNotAvailableException(keyLength)
        }

        // get initialization vector
        var iv: ByteArray? = null
        try {
            iv = encrypt!!.parameters.getParameterSpec<IvParameterSpec>(IvParameterSpec::class.java).iv
        } catch (impossible: InvalidParameterSpecException) {
        }

        // write authentication and AES initialization data
        output.write(keyLength / 8)
        output.write(salt)
        output.write(keys!!.authentication.encoded)
        output.write(iv!!)

        // read data from input into buffer, encrypt and write to output
        val buffer = ByteArray(BUFFER_SIZE)
        var numRead: Int = 0
        var encrypted: ByteArray? = null
        numRead = input.read(buffer)
        do{
            encrypted = encrypt!!.update(buffer, 0, numRead)
            if (encrypted != null) {
                output.write(encrypted)
            }
            numRead = input.read(buffer)
        } while ( numRead > 0)
        try { // finish encryption - do final block
            encrypted = encrypt!!.doFinal()
        } catch (impossible: IllegalBlockSizeException) {
        } catch (impossible: BadPaddingException) {
        }

        if (encrypted != null) {
            output.write(encrypted)
        }
    }

    /**
     * Decrypts a stream of data that was encrypted by [.encrypt].
     * @param password
     * the password used to encrypt/decrypt the stream
     * @param input
     * stream of encrypted data to be decrypted
     * @param output
     * stream to which decrypted data will be written
     * @return the key length for the decrypted stream (128, 192, or 256)
     * @throws InvalidPasswordException
     * if the given password was not used to encrypt the data
     * @throws InvalidAESStreamException
     * if the given input stream is not a valid AES-encrypted stream
     * @throws StrongEncryptionNotAvailableException
     * if the stream is 192 or 256-bit encrypted, and the Java runtime's
     * jurisdiction policy files do not allow for AES-192 or 256
     * @throws IOException
     */
    @Throws(CryptUtils.InvalidPasswordException::class, CryptUtils.InvalidAESStreamException::class, IOException::class, CryptUtils.StrongEncryptionNotAvailableException::class)
    fun decrypt(password: CharArray, input: InputStream, output: OutputStream): Int {
        val keyLength = input.read() * 8
        // Check validity of key length
        if (keyLength != 128 && keyLength != 192 && keyLength != 256) {
            throw InvalidAESStreamException()
        }

        // read salt, generate keys, and authenticate password
        val salt = ByteArray(SALT_LENGTH)
        input.read(salt)
        val keys = keygen(keyLength, password, salt)
        val authRead = ByteArray(AUTH_KEY_LENGTH)
        input.read(authRead)
        if (!Arrays.equals(keys!!.authentication.encoded, authRead)) {
            throw InvalidPasswordException()
        }

        // initialize AES decryption
        val iv = ByteArray(16) // 16-byte I.V. regardless of key size
        input.read(iv)
        var decrypt: Cipher? = null
        try {
            decrypt = Cipher.getInstance(CIPHER_SPEC)
            decrypt!!.init(Cipher.DECRYPT_MODE, keys.encryption, IvParameterSpec(iv))
        } catch (impossible: NoSuchAlgorithmException) {
        } catch (impossible: NoSuchPaddingException) {
        } catch (impossible: InvalidAlgorithmParameterException) {
        } catch (e: InvalidKeyException) { // 192 or 256-bit AES not available
            throw StrongEncryptionNotAvailableException(keyLength)
        }

        // read data from input into buffer, decrypt and write to output
        val buffer = ByteArray(BUFFER_SIZE)
        var numRead: Int
        var decrypted: ByteArray?
        numRead = input.read(buffer)
         do{
            decrypted = decrypt!!.update(buffer, 0, numRead)
            if (decrypted != null) {
                output.write(decrypted)
            }
             numRead = input.read(buffer)
        }while (numRead > 0)
        try { // finish decryption - do final block
            decrypted = decrypt!!.doFinal()
        } catch (e: IllegalBlockSizeException) {
            throw InvalidAESStreamException(e)
        } catch (e: BadPaddingException) {
            throw InvalidAESStreamException(e)
        }

        if (decrypted != null) {
            output.write(decrypted)
        }

        return keyLength
    }

    /**
     * A tuple of encryption and authentication keys returned by [.keygen]
     */
    private class Keys(val encryption: SecretKey, val authentication: SecretKey)


    //******** EXCEPTIONS thrown by encrypt and decrypt ********

    /**
     * Thrown if an attempt is made to decrypt a stream with an incorrect password.
     */
    class InvalidPasswordException : Exception()

    /**
     * Thrown if an attempt is made to encrypt a stream with an invalid AES key length.
     */
    class InvalidKeyLengthException internal constructor(length: Int) : Exception("Invalid AES key length: $length")

    /**
     * Thrown if 192- or 256-bit AES encryption or decryption is attempted,
     * but not available on the particular Java platform.
     */
    class StrongEncryptionNotAvailableException(keySize: Int) : Exception(keySize.toString() + "-bit AES encryption is not available on this Java platform.")

    /**
     * Thrown if an attempt is made to decrypt an invalid AES stream.
     */
    class InvalidAESStreamException : Exception {
        constructor() : super() {}
        constructor(e: Exception) : super(e) {}
    }

}
