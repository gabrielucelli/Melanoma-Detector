package br.com.gabrieucelli.melanomadetector.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import br.com.gabrieucelli.melanomadetector.ImagePicker
import br.com.gabrieucelli.melanomadetector.R
import br.com.gabrieucelli.melanomadetector.setStatusBarTranslucent
import br.com.gabrieucelli.melanomadetector.showToast
import br.com.gabrieucelli.melanomadetector.ui.ResultHolder
import br.com.gabrieucelli.melanomadetector.ui.preview.PreviewActivity
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.wonderkiln.camerakit.CameraKitImage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity() {

    private val RESULT_LOAD_IMG: Int = 123
    private var openPreview = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setStatusBarTranslucent()
    }

    fun imageCaptured(image: CameraKitImage) {
        Handler().post({
            image.bitmap?.let { setImageHolder(it) }
            runOnUiThread {
                PreviewActivity.start(this@MainActivity)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (openPreview) {
            PreviewActivity.start(this@MainActivity)
            openPreview = false
        } else {
            cameraView.start()
        }
    }

    override fun onStop() {
        cameraView.stop()
        super.onStop()
    }

    @OnTouch(R.id.focusMarker)
    fun onTouchMarker(view: View, motionEvent: MotionEvent): Boolean {
        focusMarker.focus(motionEvent.x, motionEvent.y)
        return false
    }

    @OnClick(R.id.open_galeria)
    fun openGaleria() {
        val chooseImageIntent = ImagePicker.getPickImageIntent(this)
        startActivityForResult(chooseImageIntent, RESULT_LOAD_IMG)
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(reqCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
                && reqCode == RESULT_LOAD_IMG
                && data != null) {

            try {
                val bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
                openPreview = true
                Handler().post({ setImageHolder(bitmap) })

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                showToast("Aconteceu alguma coisa errada :(")
            }
        }
    }

    @OnClick(R.id.button_ok)
    fun capturePhoto() {
        cameraView.captureImage { event -> imageCaptured(event) }
    }

    private fun setImageHolder(bitmap: Bitmap) {
        ResultHolder.dispose()
        ResultHolder.setImage(bitmap)
    }
}
