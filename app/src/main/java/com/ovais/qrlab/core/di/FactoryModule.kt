package com.ovais.qrlab.core.di

import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.barcode_manger.DefaultBarcodeManager
import com.ovais.qrlab.features.home.domain.CardItemsUseCase
import com.ovais.qrlab.features.home.domain.DefaultCardItemsUseCase
import org.koin.dsl.bind
import org.koin.dsl.module


val factoryModule = module {
    factory { DefaultBarcodeManager(get(), get()) } bind BarcodeManager::class
    factory { DefaultCardItemsUseCase() } bind CardItemsUseCase::class
}