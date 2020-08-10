package ru.zkv.barcodescanner.di

import com.google.android.gms.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.zkv.barcodescanner.domain.usecase.UriImageAnalyzer

internal val barcodeScannerModule = module {
    single { BarcodeScanning.getClient(get()) }

    single {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
    }

    single { UriImageAnalyzer(context = androidContext()) }
}