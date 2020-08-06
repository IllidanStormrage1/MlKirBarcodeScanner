package ru.zkv.barcodescanner.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Barcode(
    val displayValue: String?,
    val format: Int?,
    val rawValue: String?
) : Parcelable