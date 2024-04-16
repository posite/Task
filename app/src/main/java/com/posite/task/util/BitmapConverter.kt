package com.posite.task.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import java.io.ByteArrayOutputStream
import java.util.Base64


object BitmapConverter {
    // Color -> Grayscale
    fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // 흑백 필터를 생성합니다.
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)

        // 흑백 필터를 적용할 Paint를 생성합니다.
        val paint = Paint()
        paint.colorFilter = filter

        // 비트맵을 새로운 비트맵에 그려 흑백으로 변환합니다.
        val resultBitmap = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return resultBitmap
    }

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