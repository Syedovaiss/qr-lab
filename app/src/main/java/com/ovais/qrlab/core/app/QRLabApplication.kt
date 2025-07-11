package com.ovais.qrlab.core.app

import android.app.Application
import com.ovais.qrlab.core.di.factoryModule
import com.ovais.qrlab.core.di.singletonModule
import com.ovais.qrlab.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QRLabApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@QRLabApplication)
            modules(singletonModule, viewModelModule, factoryModule)
        }
    }
}