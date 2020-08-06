package ru.zkv.barcodescanner.domain.model

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

class PreparedCamera(
    val processCameraProvider: ProcessCameraProvider,
    val preview: Preview,
    val selector: CameraSelector,
    val analyzer: ImageAnalysis
)