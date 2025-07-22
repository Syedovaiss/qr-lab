package com.ovais.qrlab.core.di

import com.ovais.qrlab.barcode_manger.BarcodeManager
import com.ovais.qrlab.barcode_manger.DefaultBarcodeManager
import com.ovais.qrlab.features.create.data.CreateCodeRepository
import com.ovais.qrlab.features.create.domain.CodeFormatUseCase
import com.ovais.qrlab.features.create.domain.CodeTypeUseCase
import com.ovais.qrlab.features.create.domain.CodeValidationUseCase
import com.ovais.qrlab.features.create.domain.CreateCodeUseCase
import com.ovais.qrlab.features.create.domain.DefaultCodeFormatUseCase
import com.ovais.qrlab.features.create.domain.DefaultCodeTypeUseCase
import com.ovais.qrlab.features.create.domain.DefaultCodeValidationUseCase
import com.ovais.qrlab.features.create.domain.DefaultCreateCodeRepository
import com.ovais.qrlab.features.create.domain.DefaultCreateCodeUseCase
import com.ovais.qrlab.features.home.domain.CardItemsUseCase
import com.ovais.qrlab.features.home.domain.DefaultCardItemsUseCase
import com.ovais.qrlab.features.scan_qr.data.ScanRepository
import com.ovais.qrlab.features.scan_qr.domain.DefaultScanCodeUseCase
import com.ovais.qrlab.features.scan_qr.domain.DefaultScanRepository
import com.ovais.qrlab.features.scan_qr.domain.ScanCodeUseCase
import org.koin.dsl.bind
import org.koin.dsl.module


val factoryModule = module {
    factory { DefaultBarcodeManager(get(), get(), get()) } bind BarcodeManager::class

    // Repositories
    factory { DefaultCreateCodeRepository(get(), get()) } bind CreateCodeRepository::class
    factory { DefaultScanRepository(get()) } bind ScanRepository::class
    //Use cases
    factory { DefaultCardItemsUseCase() } bind CardItemsUseCase::class
    factory { DefaultCodeTypeUseCase() } bind CodeTypeUseCase::class
    factory { DefaultCodeFormatUseCase() } bind CodeFormatUseCase::class
    factory { DefaultCreateCodeUseCase(get()) } bind CreateCodeUseCase::class
    factory { DefaultCodeValidationUseCase() } bind CodeValidationUseCase::class
    factory { DefaultScanCodeUseCase(get()) } bind ScanCodeUseCase::class


}