package com.ovais.quickcode.core.app

import android.app.Application
import com.ovais.quickcode.core.di.factoryModule
import com.ovais.quickcode.core.di.singletonModule
import com.ovais.quickcode.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QuickCodeApplication : Application() {

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
            androidContext(this@QuickCodeApplication)
            modules(singletonModule, viewModelModule, factoryModule)
        }
    }
}