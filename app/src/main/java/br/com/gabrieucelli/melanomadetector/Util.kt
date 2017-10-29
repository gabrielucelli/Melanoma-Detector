package br.com.gabrieucelli.melanomadetector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

/**
 * Created by Gabriel on 26/10/2017.
 */

fun getRealPathFromURI(context: Context, originalUri: Uri): String {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (originalUri.path.matches("/storage.*\\.jpg".toRegex())) {

            return originalUri.path

        } else {

            var absPath = ""
            val pathsegment = originalUri.lastPathSegment.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val id = originalUri.lastPathSegment
            val imageColumns = arrayOf(MediaStore.Images.Media.DATA)

            val uri = uri

            try {
                val imageCursor = context.contentResolver.query(uri, imageColumns,
                        MediaStore.Images.Media._ID + "=" + id, null, null)

                if (imageCursor!!.moveToFirst()) {
                    absPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                }

                return absPath
            } catch (e: Exception) {
                return ""
            }

        }
    } else {
        return originalUri.path
    }
}

private val uri: Uri
    get() {

        val state = Environment.getExternalStorageState()

        if (!state.equals(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

fun loadImage(url: String): Bitmap? {
    val selectedImage: Bitmap = BitmapFactory.decodeFile(url)
    return selectedImage
}

fun scaleToSizeWindow(context: Context, bitmap: Bitmap): Bitmap {

    val height = context.getResources().getDisplayMetrics().heightPixels
    val width = context.getResources().getDisplayMetrics().widthPixels

    Log.d("scaleToSizeWindow", "height: $height width: $width")
    Log.d("scaleToSizeWindow", "bitmap -> height: ${bitmap.height} width: ${bitmap.width}")

    val scale: Float = if (bitmap.height > bitmap.width) {
        bitmap.height / height.toFloat()
    } else {
        bitmap.width / width.toFloat()
    }

    Log.d("scaleToSizeWindow", "scale : $scale")

    return Bitmap.createScaledBitmap(bitmap,
            (bitmap.width / scale).toInt(),
            (bitmap.height / scale).toInt(),
            true)
}