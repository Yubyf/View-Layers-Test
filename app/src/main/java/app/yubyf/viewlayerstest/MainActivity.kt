package app.yubyf.viewlayerstest

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

class MainActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var layerTestView: LayerTestView
    private lateinit var tvLayerType: TextView

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas
    private lateinit var modifiedBitmap: Bitmap
    private lateinit var modifiedBitmapCanvas: Canvas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRes()
    }

    private fun initView() {
        layerTestView = findViewById(R.id.layer_test_view)
        tvLayerType = findViewById(R.id.tv_layer_type_value)
        layerTestView.onLayerTypeChanged = {
            tvLayerType.text = when (it) {
                View.LAYER_TYPE_NONE -> "NONE"
                View.LAYER_TYPE_HARDWARE -> "HARDWARE"
                View.LAYER_TYPE_SOFTWARE -> "SOFTWARE"
                else -> "NONE"
            }
        }
        tvLayerType.text = when (layerTestView.layerType) {
            View.LAYER_TYPE_NONE -> "NONE"
            View.LAYER_TYPE_HARDWARE -> "HARDWARE"
            View.LAYER_TYPE_SOFTWARE -> "SOFTWARE"
            else -> "NONE"
        }

        radioGroup = findViewById(R.id.radio_group_layer_types)
        radioGroup.check(when (layerTestView.layerType) {
            View.LAYER_TYPE_NONE -> R.id.btn_layer_type_none
            View.LAYER_TYPE_HARDWARE -> R.id.btn_layer_type_hardware
            View.LAYER_TYPE_SOFTWARE -> R.id.btn_layer_type_software
            else -> R.id.btn_layer_type_none
        })
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_layer_type_none -> layerTestView.setLayerType(View.LAYER_TYPE_NONE, null)
                R.id.btn_layer_type_hardware -> layerTestView.setLayerType(View.LAYER_TYPE_HARDWARE,
                    null)
                R.id.btn_layer_type_software -> layerTestView.setLayerType(View.LAYER_TYPE_SOFTWARE,
                    null)
                else -> {}
            }
        }

        findViewById<Button>(R.id.btn_reset_bitmap).setOnClickListener {
            modifiedBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            modifiedBitmapCanvas.drawBitmap(bitmap, 0F, 0F, null)
            layerTestView.bitmap = modifiedBitmap
        }
        findViewById<Button>(R.id.btn_modify_bitmap_pixels).setOnClickListener {
            layerTestView.modifyBitmapPixels()
        }
        findViewById<Button>(R.id.btn_clear_bitmap_pixels).setOnClickListener {
            layerTestView.clearBitmapPixels()
        }
    }

    private fun initRes() {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.ic_android)
        drawable?.setBounds(
            0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight
        )
        bitmap =
            Bitmap.createBitmap(
                drawable?.intrinsicWidth ?: 100,
                drawable?.intrinsicHeight ?: 100,
                Bitmap.Config.ARGB_8888
            )
        bitmapCanvas = Canvas(bitmap)
        drawable?.draw(bitmapCanvas)
        modifiedBitmap = Bitmap.createBitmap(bitmap)
        modifiedBitmapCanvas = Canvas(modifiedBitmap)
        layerTestView.bitmap = modifiedBitmap
        findViewById<ImageView>(R.id.iv_sample).setImageBitmap(modifiedBitmap)
    }
}