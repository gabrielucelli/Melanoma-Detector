package br.com.gabrieucelli.melanomadetector.ui

import android.graphics.Bitmap

/**
 * Created by Gabriel on 26/10/2017.
 */

object ResultHolder {

    private var image: Bitmap? = null

    fun setImage(image: Bitmap) {
        this.image = image
    }

    fun getImage(): Bitmap? {
        return image
    }

    fun dispose() {
        image?.recycle()
        image = null
    }

}