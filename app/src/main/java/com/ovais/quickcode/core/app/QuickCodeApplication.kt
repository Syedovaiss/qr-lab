package com.ovais.quickcode.core.app

import android.app.Application
import androidx.work.Configuration
import com.ovais.quickcode.core.di.factoryModule
import com.ovais.quickcode.core.di.singletonModule
import com.ovais.quickcode.core.di.viewModelModule
import com.ovais.quickcode.worker.factory.AppWorkerFactory
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QuickCodeApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        initSQLCipher()
        initKoin()
        initTimber()
    }

    private fun initSQLCipher() {
        System.loadLibrary("sqlcipher")
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


    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(getKoin().get<AppWorkerFactory>())
            .build()

}