package fr.quintipio.simplyPassword.com


import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ComFile(path: String) {

    val file = File(path)


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

    fun writeBytes(data: ByteArray): Boolean {
        return try {
            val stream = FileOutputStream(file)
            stream.write(data)
            stream.close()
            true
        } catch (e: Exception) {
            false
        }

    }

    fun readString(): String {
        return try {
            val length = file.length().toInt()

            val bytes = ByteArray(length)

            val ind = FileInputStream(file)
            ind.use { ind ->
                ind.read(bytes)
            }

            String(bytes)
        } catch (e: Exception) {
            ""
        }

    }

    fun readBytes(): ByteArray? {
        return try {
            val length = file.length().toInt()
            val bytes = ByteArray(length)
            val ind = FileInputStream(file)
            ind.use { ind ->
                ind.read(bytes)
            }
            bytes
        } catch (e: Exception) {
            null
        }

    }
}
