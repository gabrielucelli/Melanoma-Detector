package br.com.gabrieucelli.melanomadetector.ui.main

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import br.com.gabrieucelli.melanomadetector.R
import br.com.gabrieucelli.melanomadetector.ui.ResultHolder
import br.com.gabrieucelli.melanomadetector.ui.preview.PreviewActivity
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.wonderkiln.camerakit.CameraListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setStatusBarTranslucent()
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

    private fun Activity.setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    @OnTouch(R.id.focusMarker)
    fun onTouchMarker(view: View, motionEvent: MotionEvent): Boolean {
        focusMarker.focus(motionEvent.x, motionEvent.y)
        return false
    }

    @OnClick(R.id.button_ok)
    fun capturePhoto() {

        cameraView.setCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray) {
                super.onPictureTaken(jpeg)
                val bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.size)
                ResultHolder.dispose()
                ResultHolder.setImage(bitmap)
                PreviewActivity.start(this@MainActivity)
            }
        })

        cameraView.captureImage()
    }
}
