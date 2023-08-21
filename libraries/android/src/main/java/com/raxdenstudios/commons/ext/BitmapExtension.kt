package com.raxdenstudios.commons.ext

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(
    maxSize: Int,
    quality: Int,
    flags: Int = Base64.DEFAULT,
): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    resize(maxSize).compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), flags)
}

fun Bitmap.rotate(rotation: Int): Bitmap {
    if (rotation == 0) return this

    val matrix = Matrix()
    matrix.postRotate(rotation.toFloat())
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.resize(size: Int): Bitmap {
    var width = width.toFloat()
    var height = height.toFloat()
    val aspectRatio: Float

    if (width > height) {
        aspectRatio = width / height
        if (width > size) width = size.toFloat()
        height = width / aspectRatio
    } else {
        aspectRatio = height / width
        if (height > size) height = size.toFloat()
        width = height / aspectRatio
    }

    return Bitmap.createScaledBitmap(this, width.toInt(), height.toInt(), true)
}
