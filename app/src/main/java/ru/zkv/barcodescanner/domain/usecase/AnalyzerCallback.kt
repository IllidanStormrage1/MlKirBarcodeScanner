package ru.zkv.barcodescanner.domain.usecase

import ru.zkv.barcodescanner.domain.model.Barcode

interface AnalyzerCallback {
    fun onSuccess(barcode: Barcode)
    fun onFailure(exception: Exception)
}