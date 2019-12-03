@file:Suppress("unused")

package coil.transform

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import coil.bitmappool.BitmapPool

/**
 * A [Transformation] that rotates an image by a given number of degrees.
 *
 * @param rotation The number of degrees by which to rotate the image.
 */
class RotateTransformation(private val rotation: Float) : Transformation {

    init {
        require(rotation > 0f) { "Rotation must be greater than 0f." }
        require(rotation < 360f) { "Rotation must be les than 360f." }
    }

    override fun key(): String = "${RotateTransformation::class.java.name}-$rotation"

    override suspend fun transform(pool: BitmapPool, input: Bitmap): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val rotated = input.rotate(rotation)
        val output = pool.get(rotated.width, rotated.height, input.config)
        output.applyCanvas {
            drawBitmap(rotated, 0f, 0f, paint)
        }
        pool.put(input)

        return output
    }
}

private fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
