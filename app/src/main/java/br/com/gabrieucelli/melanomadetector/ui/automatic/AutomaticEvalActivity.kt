package br.com.gabrieucelli.melanomadetector.ui.automatic

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import br.com.gabrieucelli.melanomadetector.ImageUtils
import br.com.gabrieucelli.melanomadetector.R
import br.com.gabrieucelli.melanomadetector.classifier.Classifier
import br.com.gabrieucelli.melanomadetector.classifier.TensorFlowImageClassifier
import br.com.gabrieucelli.melanomadetector.ui.ResultHolder
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_automatic_eval.*


class AutomaticEvalActivity : AppCompatActivity() {

    private val INPUT_SIZE = 224
    private val IMAGE_MEAN = 127
    private val IMAGE_STD = 1f

    private val INPUT_NAME = "input"
    private val OUTPUT_NAME = "final_result"

    private val MODEL_FILE = "file:///android_asset/optimized_graph.pb"
    private val LABEL_FILE = "file:///android_asset/retrained_labels.txt"

    private val handlerThread: HandlerThread by lazy { HandlerThread("inference").apply { start() } }
    private val handler: Handler by lazy { Handler(handlerThread.looper) }
    private val resultAdapter by lazy { ResultAdapter(this) }

    private val classifier: Classifier by lazy {
        TensorFlowImageClassifier.create(
                assets,
                MODEL_FILE,
                LABEL_FILE,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, AutomaticEvalActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automatic_eval)
        ButterKnife.bind(this)
        setupToolbar()
        showImage()
        setupRecyclerResults()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Avaliação Automática"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun showImage() {
        image_view.setImageBitmap(ResultHolder.getImage())
    }

    private fun setupRecyclerResults() {
        val layoutManager = LinearLayoutManager(this)
        recycler_result.adapter = resultAdapter
        recycler_result.layoutManager = layoutManager
    }

    public override fun onPause() {

        try {
            handlerThread.interrupt()
            handlerThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        super.onPause()
    }

    override fun onStart() {

        super.onStart()

        runInBackground(
                Runnable {
                    ResultHolder.getImage()
                            ?.let { classifier.recognizeImage(scaleToInputSize(it)) }
                            ?.also { runOnUiThread { showResults(it) } }
                })
    }

    private fun showResults(list: List<Classifier.Recognition>) {
        resultAdapter.setList(list)
    }

    private fun scaleToInputSize(bitmap: Bitmap): Bitmap {

        val frameToCropTransform = ImageUtils.getTransformationMatrix(
                bitmap.width, bitmap.height,
                INPUT_SIZE, INPUT_SIZE,
                0, true)

        val cropToFrameTransform = Matrix()
        frameToCropTransform.invert(cropToFrameTransform)

        val newBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawBitmap(bitmap, frameToCropTransform, null)

        return newBitmap
    }

    protected fun runInBackground(r: Runnable) {
        handler.post(r)
    }

    @OnClick(R.id.button_ok)
    fun onClickOk() {
        onBackPressed()
    }
}
