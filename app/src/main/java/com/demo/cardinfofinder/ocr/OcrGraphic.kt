package com.demo.cardinfofinder.ocr

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.demo.cardinfofinder.ui.camera.GraphicOverlay
import com.demo.cardinfofinder.ui.camera.GraphicOverlay.Graphic
import com.google.android.gms.vision.text.TextBlock

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class OcrGraphic internal constructor(overlay: GraphicOverlay<*>?, val textBlock: TextBlock?) : Graphic(overlay) {
    var id = 0

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    override fun contains(x: Float, y: Float): Boolean {
        if (textBlock == null) {
            return false
        }
        var rect = RectF(textBlock.boundingBox)
        rect = translateRect(rect)
        return rect.contains(x, y)
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas?) {
        if (textBlock == null) {
            return
        }

        // Draws the bounding box around the TextBlock.
        var rect: RectF? = RectF(textBlock.boundingBox)
        rect = rect?.let { translateRect(it) }
        if (rect != null) {
            canvas?.drawRect(rect, rectPaint!!)
        }

        // Break the text into multiple lines and draw each one according to its own bounding box.
        val textComponents = textBlock.components
        for (currentText in textComponents) {
            val left = translateX(currentText.boundingBox.left.toFloat())
            val bottom = translateY(currentText.boundingBox.bottom.toFloat())
            canvas?.drawText(currentText.value, left, bottom, textPaint!!)
        }
    }

    companion object {
        private const val TEXT_COLOR = Color.WHITE
        private var rectPaint: Paint? = null
        private var textPaint: Paint? = null
    }

    init {
        if (rectPaint == null) {
            rectPaint = Paint()
            rectPaint!!.color = TEXT_COLOR
            rectPaint!!.style = Paint.Style.STROKE
            rectPaint!!.strokeWidth = 4.0f
        }
        if (textPaint == null) {
            textPaint = Paint()
            textPaint!!.color = TEXT_COLOR
            textPaint!!.textSize = 54.0f
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }
}