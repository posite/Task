package com.posite.task.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.util.Base64


object BitmapConverter {
    // Bitmap -> String
    fun bitmapToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val bytes = stream.toByteArray()

        return Base64.getEncoder().encodeToString(bytes)
    }

    fun stringToBitmap(img: String): Bitmap? {
        return try {
            val imageBytes = Base64.getDecoder().decode(img)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            image
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}