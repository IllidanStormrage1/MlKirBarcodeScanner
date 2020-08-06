package ru.zkv.barcodescanner.domain.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes string: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, length).show()
}

fun Context.showToast(string: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, length).show()
}