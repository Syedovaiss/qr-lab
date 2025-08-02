package com.ovais.quickcode.core.app

import android.app.Application
import com.ovais.quickcode.core.di.DEFAULT
import com.ovais.quickcode.core.di.factoryModule
import com.ovais.quickcode.core.di.singletonModule
import com.ovais.quickcode.core.di.viewModelModule
import com.ovais.quickcode.locale.DefaultAppLocaleManager
import com.ovais.quickcode.notification.DefaultQuickCodeNotificationManager
import com.ovais.quickcode.storage.DefaultQuickCodeConfigurationManager
import com.ovais.quickcode.storage.db.ConfigurationDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import timber.log.Timber

class QuickCodeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
        initNotifications()
        initRemoteConfig()
        initSQLCipher()
        initLocale()
    }

    private fun initSQLCipher() {
        System.loadLibrary("sqlcipher")
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initRemoteConfig() {
        val dispatcherDefault = getKoin().get<CoroutineDispatcher>(named(DEFAULT))
        val config = getKoin().get<DefaultQuickCodeConfigurationManager>()
        GlobalScope.launch(dispatcherDefault) {
            config.activate()
        }
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

    private fun initNotifications() {
        val manager = getKoin().get<DefaultQuickCodeNotificationManager>()
        manager.init()
    }

    private fun initLocale() {
        val localeManager = getKoin().get<DefaultAppLocaleManager>()
        val configurationDao = getKoin().get<ConfigurationDao>()
        runBlocking {
            configurationDao.getLocale()?.let {
                localeManager.setLocale(it)
            }
        }
    }

}