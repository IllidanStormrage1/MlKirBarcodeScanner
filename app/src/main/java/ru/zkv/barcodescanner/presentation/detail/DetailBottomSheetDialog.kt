package ru.zkv.barcodescanner.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zkv.barcodescanner.R
import ru.zkv.barcodescanner.databinding.DetailBottomSheetDialogBinding
import ru.zkv.barcodescanner.domain.model.Barcode

class DetailBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DetailBottomSheetDialogBinding

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DetailBottomSheetDialogBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val barcode: Barcode? = it.getParcelable(barcodeKey)
            binding.barcode = barcode
            binding.detailTvSearch.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.detail_google_url).format(barcode?.displayValue))))
            }
        }
    }

    companion object {
        val TAG: String = DetailBottomSheetDialog::class.java.simpleName
        private const val barcodeKey = "barcode"

        @JvmStatic
        fun newInstance(barcode: Barcode) = DetailBottomSheetDialog().apply {
            arguments = bundleOf(barcodeKey to barcode)
        }
    }
}