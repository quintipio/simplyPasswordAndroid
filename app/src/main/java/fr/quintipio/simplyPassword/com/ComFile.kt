package fr.quintipio.simplyPassword.com


import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ComFile(path: String) {

    val file: File
    init {
        this.file = File(path)
    }

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
        try {
            val stream = FileOutputStream(file)
            stream.write(data)
            stream.close()
            return true
        } catch (e: Exception) {
            return false
        }

    }

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
