package com.example.lab2

import android.content.Context
import java.io.File

object StorageHelper {

    private const val FILE_NAME = "lab2_storage.txt"

    fun write(context: Context, text: String): Boolean {
        return try {
            context.openFileOutput(FILE_NAME, Context.MODE_APPEND).use {
                it.write((text + "\n\n").toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun read(context: Context): String {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return ""
        return file.readText()
    }

    fun clear(context: Context): Boolean {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) file.writeText("")
            true
        } catch (e: Exception) {
            false
        }
    }
}
