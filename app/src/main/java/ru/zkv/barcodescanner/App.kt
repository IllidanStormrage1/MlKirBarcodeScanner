package ru.zkv.barcodescanner

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.zkv.barcodescanner.di.barcodeScannerModule
import ru.zkv.barcodescanner.di.viewModelModule

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(level = Level.DEBUG)
            modules(modules = viewModelModule + barcodeScannerModule)
        }
    }
}