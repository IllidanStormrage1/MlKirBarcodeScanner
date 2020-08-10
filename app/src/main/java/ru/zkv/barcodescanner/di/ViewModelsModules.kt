package ru.zkv.barcodescanner.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.zkv.barcodescanner.presentation.main.MainAndroidViewModel

internal val viewModelModule = module {
    viewModel { MainAndroidViewModel(androidApplication(), get()) }
}