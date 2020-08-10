package ru.zkv.barcodescanner.domain.usecase

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.zkv.barcodescanner.domain.model.Barcode

class UriImageAnalyzer(private val context: Context) : KoinComponent {

    private val barcodeScanner: BarcodeScanner by inject()

    internal fun analyze(uri: Uri, callback: AnalyzerCallback) {
        barcodeScanner.process(InputImage.fromFilePath(context, uri))
            .addOnSuccessListener {
                if (it.isNotEmpty()) {
                    val firstItem = it.first()
                    callback.onSuccess(
                        Barcode(
                            firstItem.displayValue,
                            firstItem.format,
                            firstItem.rawValue
                        )
                    )
                }
            }.addOnFailureListener {
                callback.onFailure(it)
            }
    }
}