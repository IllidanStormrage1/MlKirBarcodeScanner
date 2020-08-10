package ru.zkv.barcodescanner.domain.usecase

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.zkv.barcodescanner.domain.model.Barcode

class ImageAnalyzer(private val callback: AnalyzerCallback) :
    ImageAnalysis.Analyzer, KoinComponent {

    private val barcodeScanner: BarcodeScanner by inject()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        val info = image.imageInfo.rotationDegrees

        if (mediaImage != null)
            barcodeScanner.process(InputImage.fromMediaImage(mediaImage, info))
                .addOnSuccessListener {
                    if (it.isNotEmpty()) {
                        val firstBarcode = it.first()
                        callback.onSuccess(
                            Barcode(
                                firstBarcode.displayValue,
                                firstBarcode.format,
                                firstBarcode.rawValue
                            )
                        )
                    }
                }
                .addOnCompleteListener { image.close() }
                .addOnFailureListener { exception -> callback.onFailure(exception) }
    }
}