package com.ovais.quickcode.core.di

import com.ovais.quickcode.barcode_manger.BarcodeManager
import com.ovais.quickcode.barcode_manger.DefaultBarcodeManager
import com.ovais.quickcode.features.create.data.CreateCodeRepository
import com.ovais.quickcode.features.create.domain.CodeFormatUseCase
import com.ovais.quickcode.features.create.domain.CodeTypeUseCase
import com.ovais.quickcode.features.create.domain.CodeValidationUseCase
import com.ovais.quickcode.features.create.domain.CreateCodeUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeFormatUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeTypeUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeValidationUseCase
import com.ovais.quickcode.features.create.domain.DefaultCreateCodeRepository
import com.ovais.quickcode.features.create.domain.DefaultCreateCodeUseCase
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultCardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultGetUserInfoUseCase
import com.ovais.quickcode.features.home.domain.GetUserInfoUseCase
import com.ovais.quickcode.features.scan_qr.data.ScanRepository
import com.ovais.quickcode.features.scan_qr.domain.DefaultScanCodeUseCase
import com.ovais.quickcode.features.scan_qr.domain.DefaultScanRepository
import com.ovais.quickcode.features.scan_qr.domain.ScanCodeUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


val factoryModule = module {
    factory {
        DefaultGetUserInfoUseCase(
            get(),
            get(),
            get()
        )
    } bind GetUserInfoUseCase::class

    factory {
        DefaultBarcodeManager(
            get(named(DEFAULT)),
            get(),
            get()
        )
    } bind BarcodeManager::class

    // Repositories
    factory {
        DefaultCreateCodeRepository(
            get(),
            get(named(BACKGROUND))
        )
    } bind CreateCodeRepository::class
    factory {
        DefaultScanRepository(
            get()
        )
    } bind ScanRepository::class
    //Use cases
    factory { DefaultCardItemsUseCase() } bind CardItemsUseCase::class
    factory { DefaultCodeTypeUseCase() } bind CodeTypeUseCase::class
    factory { DefaultCodeFormatUseCase() } bind CodeFormatUseCase::class
    factory { DefaultCreateCodeUseCase(get()) } bind CreateCodeUseCase::class
    factory { DefaultCodeValidationUseCase() } bind CodeValidationUseCase::class
    factory { DefaultScanCodeUseCase(get()) } bind ScanCodeUseCase::class


}