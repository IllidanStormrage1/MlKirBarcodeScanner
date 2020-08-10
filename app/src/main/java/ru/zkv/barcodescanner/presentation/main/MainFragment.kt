package ru.zkv.barcodescanner.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.FocusMeteringAction
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.zkv.barcodescanner.R
import ru.zkv.barcodescanner.databinding.MainFragmentBinding
import ru.zkv.barcodescanner.domain.model.PreparedCamera
import ru.zkv.barcodescanner.domain.utils.showToast
import ru.zkv.barcodescanner.presentation.detail.DetailBottomSheetDialog

class MainFragment : Fragment() {

    private val androidViewModel: MainAndroidViewModel by viewModel { parametersOf() }
    private lateinit var binding: MainFragmentBinding
    private var preparedCamera: PreparedCamera? = null
    private var camera: Camera? = null

    private val permissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                androidViewModel.prepareCamera()
            } else {
                requireActivity().showToast(R.string.permission_denied_text)
            }
        }
    private val pickImageCallback =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null)
                androidViewModel.analyzeImage(it)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        MainFragmentBinding.inflate(layoutInflater).apply {
            vm = androidViewModel
            lifecycleOwner = this@MainFragment
        }.also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionCallback.launch(Manifest.permission.CAMERA)

        configureCamera()

        binding.mainBottomBar.setNavigationOnClickListener {
            pickImageCallback.launch("image/*")
        }

        androidViewModel.barcodeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Success -> {
                    if (parentFragmentManager.findFragmentByTag(DetailBottomSheetDialog.TAG) == null)
                        DetailBottomSheetDialog.newInstance(it.payload)
                            .show(parentFragmentManager, DetailBottomSheetDialog.TAG)
                }
                is UIState.Failure -> {
                    requireActivity().showToast(string = it.exception.message)
                }
                is UIState.Loading -> {
                    progress_circular.isVisible = it.isVisible
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureCamera() {
        androidViewModel.cameraProviderLiveData.observe(viewLifecycleOwner) {
            it.run {
                processCameraProvider.unbindAll()
                camera = processCameraProvider.bindToLifecycle(
                    this@MainFragment,
                    selector,
                    preview,
                    analyzer
                )
                preview.setSurfaceProvider(binding.mainCamera.createSurfaceProvider())
                preparedCamera = it
            }
        }

        binding.mainCamera.setOnTouchListener { view, motionEvent ->
            preparedCamera?.let {
                if (motionEvent.action != MotionEvent.ACTION_UP) return@let
                val factory = binding.mainCamera.createMeteringPointFactory(it.selector)
                val point = factory.createPoint(motionEvent.x, motionEvent.y)
                val action = FocusMeteringAction.Builder(point).build()
                camera?.cameraControl?.startFocusAndMetering(action)
            }
            return@setOnTouchListener true
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}