package ru.zkv.barcodescanner.presentation.main

import ru.zkv.barcodescanner.domain.model.Barcode

sealed class UIState {
    class Loading(val isVisible: Boolean) : UIState()
    class Success(val payload: Barcode) : UIState()
    class Failure(val exception: Exception) : UIState()
}