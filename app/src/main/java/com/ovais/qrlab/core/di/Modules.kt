package com.ovais.qrlab.core.di

import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.barcode_manger.DefaultBarcodeManager
import com.ovais.qrlab.logger.DefaultQRLogger
import com.ovais.qrlab.logger.QRLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.bind
import org.koin.dsl.module


val singletonModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
    single<CoroutineDispatcher> {
        Dispatchers.Default
    }
    single<CoroutineDispatcher> {
        Dispatchers.Main
    }
    single { DefaultQRLogger() } bind QRLogger::class
}

val factoryModule = module {
    factory { DefaultBarcodeManager(get(), get()) } bind BarcodeManager::class
}
val viewModelModule = module {
//    viewModel { HomeViewModel(get()) }
}
