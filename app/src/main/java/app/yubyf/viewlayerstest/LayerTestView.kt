package app.yubyf.viewlayerstest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

/**
 * @author Yubyf
 * @date 2022/1/31.
 */
class LayerTestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var onLayerTypeChanged: ((Int) -> Unit)? = null

    private var bitmapCanvas: Canvas? = null
    var bitmap: Bitmap? = null
        set(value) {
            field = value
            value?.let {
                bitmapCanvas = Canvas(it)
                bitmapRect.set(0, 0, it.width, value.height)
            }
            invalidate()
        }

    private val bitmapRect: Rect = Rect()
    private val viewRect: Rect = Rect()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewRect.set(0, 0, w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        bitmap?.let {
            canvas?.drawBitmap(it, bitmapRect, viewRect, null)
            Toast.makeText(context, "onDraw in LayerTestView", Toast.LENGTH_SHORT).show()
        }
    }

    override fun post(action: Runnable?): Boolean {
        onLayerTypeChanged?.let { it(layerType) }
        return super.post(action)
    }

    override fun setLayerType(layerType: Int, paint: Paint?) {
        super.setLayerType(layerType, paint)
        onLayerTypeChanged?.let { it(this.layerType) }
    }

    fun modifyBitmapPixels() {
        bitmapCanvas?.run {
            drawCircle(this.width / 2F, this.height / 2F, this.width / 8F,
                Paint().also {
                    it.color = Color.RED
                    it.style = Paint.Style.FILL
                }
            )
        }
    }

    fun clearBitmapPixels() {
        bitmapCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }
}