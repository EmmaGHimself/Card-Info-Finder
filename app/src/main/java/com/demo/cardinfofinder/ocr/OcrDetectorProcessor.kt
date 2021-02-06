package com.demo.cardinfofinder.ocr

import android.util.Log
import com.demo.cardinfofinder.ui.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.Detector.Detections

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
class OcrDetectorProcessor(private val graphicOverlay: GraphicOverlay<OcrGraphic?>?) : Detector.Processor<TextBlock?> {
    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    override fun receiveDetections(detections: Detections<TextBlock?>) {
        graphicOverlay?.clear()
        val items = detections.detectedItems
        for (i in 0 until items.size()) {
            val item = items.valueAt(i)
            if (item != null && item.value != null) {
                Log.d("OcrDetectorProcessor", "Text detected! " + item.value)
                val graphic = OcrGraphic(graphicOverlay, item)
                graphicOverlay?.add(graphic)
            }
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    override fun release() {
        graphicOverlay?.clear()
    }
}