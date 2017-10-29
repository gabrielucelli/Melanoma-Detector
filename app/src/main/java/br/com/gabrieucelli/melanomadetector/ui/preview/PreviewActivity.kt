package br.com.gabrieucelli.melanomadetector.ui.preview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import br.com.gabrieucelli.melanomadetector.R
import br.com.gabrieucelli.melanomadetector.scaleToSizeWindow
import br.com.gabrieucelli.melanomadetector.ui.ResultHolder
import br.com.gabrieucelli.melanomadetector.ui.automatic.AutomaticEvalActivity
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_preview.*


class PreviewActivity : AppCompatActivity() {

    private var rawImage: Bitmap? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PreviewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setupCropImageView()
        ButterKnife.bind(this)
        setupToolbar()
    }

    override fun onStart() {
        super.onStart()
        ResultHolder.getImage()?.let { rawImage = scaleToSizeWindow(this@PreviewActivity, it) }
        crop_image_view.setImageBitmap(rawImage)
    }

    private fun setupCropImageView() {
        crop_image_view.setAspectRatio(1, 1)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "";
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    @OnClick(R.id.button_ok)
    fun openAutomticEval() {
        val bitmap = crop_image_view.croppedImage
        ResultHolder.dispose()
        ResultHolder.setImage(bitmap)
        AutomaticEvalActivity.start(this@PreviewActivity)
        finish()
    }

}

