package ru.zkv.barcodescanner.presentation.main

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import ru.zkv.barcodescanner.domain.model.Barcode
import ru.zkv.barcodescanner.domain.model.PreparedCamera
import ru.zkv.barcodescanner.domain.usecase.AnalyzerCallback
import ru.zkv.barcodescanner.domain.usecase.ImageAnalyzer
import ru.zkv.barcodescanner.domain.utils.SingleLiveData

class MainAndroidViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {

    private val _barcodeLiveData: MutableLiveData<UIState> = MutableLiveData()
    val barcodeLiveData: LiveData<UIState> get() = _barcodeLiveData

    private val _cameraProviderLiveData: MutableLiveData<PreparedCamera> = SingleLiveData()
    val cameraProviderLiveData: LiveData<PreparedCamera> get() = _cameraProviderLiveData

    internal fun prepareCamera() {
        val processCameraProvider = ProcessCameraProvider.getInstance(applicationContext)
        processCameraProvider.addListener(
            Runnable {
                val cameraProvider = processCameraProvider.get()
                val preview = Preview.Builder().build()
                val cameraSelector =
                    CameraSelector
                        .Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                val analyzer = ImageAnalysis.Builder().build()
                analyzer.setAnalyzer(
                    Dispatchers.IO.asExecutor(),
                    ImageAnalyzer(object : AnalyzerCallback {
                        override fun onSuccess(barcode: Barcode) {
                            _barcodeLiveData.value = UIState.Success(barcode)
                        }

                        override fun onFailure(exception: Exception) {
                            _barcodeLiveData.value = UIState.Failure(exception)
                        }
                    })
                )
                _cameraProviderLiveData.postValue(
                    PreparedCamera(
                        processCameraProvider = cameraProvider,
                        preview = preview,
                        selector = cameraSelector,
                        analyzer = analyzer
                    )
                )
            }, Dispatchers.IO.asExecutor()
        )
    }
}
