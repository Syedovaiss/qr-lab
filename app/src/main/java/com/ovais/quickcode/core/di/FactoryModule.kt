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
import com.ovais.quickcode.features.home.domain.CanLoginUseCase
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultCanLoginUseCase
import com.ovais.quickcode.features.home.domain.DefaultCardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultLoginResultUseCase
import com.ovais.quickcode.features.home.domain.LoginResultUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetUserInfoUseCase
import com.ovais.quickcode.utils.usecase.GetUserInfoUseCase
import com.ovais.quickcode.features.scan_code.data.ScanRepository
import com.ovais.quickcode.features.scan_code.domain.CanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultCanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultScanCodeUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultScanRepository
import com.ovais.quickcode.features.scan_code.domain.ScanCodeUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetPrivacyPolicyUseCase
import com.ovais.quickcode.features.settings.domain.DefaultUpdateSettingUseCase
import com.ovais.quickcode.utils.usecase.GetPrivacyPolicyUseCase
import com.ovais.quickcode.features.settings.domain.UpdateSettingUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetAboutUsUseCase
import com.ovais.quickcode.utils.usecase.GetAboutUsUseCase
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

    factory { DefaultLoginResultUseCase(get(), get()) } bind LoginResultUseCase::class
    factory { DefaultCanLoginUseCase(get(), get()) } bind CanLoginUseCase::class
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

    // Settings Use Case
    factory { DefaultUpdateSettingUseCase(get(), get()) } bind UpdateSettingUseCase::class

    //Use cases
    factory { DefaultCardItemsUseCase() } bind CardItemsUseCase::class
    factory { DefaultCodeTypeUseCase() } bind CodeTypeUseCase::class
    factory { DefaultCodeFormatUseCase() } bind CodeFormatUseCase::class
    factory { DefaultCreateCodeUseCase(get()) } bind CreateCodeUseCase::class
    factory { DefaultCodeValidationUseCase() } bind CodeValidationUseCase::class
    factory { DefaultScanCodeUseCase(get()) } bind ScanCodeUseCase::class
    factory { DefaultGetPrivacyPolicyUseCase(get()) } bind GetPrivacyPolicyUseCase::class
    factory { DefaultGetAboutUsUseCase(get()) } bind GetAboutUsUseCase::class
    factory {
        DefaultCanVibrateAndBeepUseCase(
            get(),
            get(named(BACKGROUND))
        )
    } bind CanVibrateAndBeepUseCase::class
}