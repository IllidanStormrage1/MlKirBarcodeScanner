package ru.zkv.barcodescanner.presentation.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.zkv.barcodescanner.R
import ru.zkv.barcodescanner.databinding.MainFragmentBinding
import ru.zkv.barcodescanner.domain.utils.showToast
import ru.zkv.barcodescanner.presentation.detail.DetailBottomSheetDialog

class MainFragment : Fragment() {

    private val androidViewModel: MainAndroidViewModel by viewModel { parametersOf() }
    private lateinit var binding: MainFragmentBinding

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
            // TODO: 06.08.20
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(layoutInflater).apply {
            vm = androidViewModel
            lifecycleOwner = this@MainFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionCallback.launch(Manifest.permission.CAMERA)

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
            }
        }

        androidViewModel.cameraProviderLiveData.observe(viewLifecycleOwner) {
            it.run {
                processCameraProvider.unbindAll()
                processCameraProvider.bindToLifecycle(
                    this@MainFragment,
                    selector,
                    preview,
                    analyzer
                )
                preview.setSurfaceProvider(binding.mainCamera.createSurfaceProvider())
            }
        }

// TODO: 06.08.20
//        binding.mainBottomBar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                android.R.id.home -> pickImageCallback.launch("images/*")
//            }
//            return@setOnMenuItemClickListener true
//        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}