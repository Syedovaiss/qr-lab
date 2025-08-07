package com.ovais.quickcode.core.di

import com.ovais.quickcode.barcode_manger.BarcodeManager
import com.ovais.quickcode.barcode_manger.DefaultBarcodeManager
import com.ovais.quickcode.core.app.domain.DefaultStartDestinationUseCase
import com.ovais.quickcode.core.app.domain.StartDestinationUseCase
import com.ovais.quickcode.features.code_details.domain.DefaultSaveScannedCodeUseCase
import com.ovais.quickcode.features.code_details.domain.SaveScannedCodeUseCase
import com.ovais.quickcode.features.create.data.CreateCodeRepository
import com.ovais.quickcode.features.create.domain.CanAutoOpenUrlUseCase
import com.ovais.quickcode.features.create.domain.CanCopyToClipboardUseCase
import com.ovais.quickcode.features.create.domain.CodeDefaultColorUseCase
import com.ovais.quickcode.features.create.domain.CodeFormatUseCase
import com.ovais.quickcode.features.create.domain.CodeTypeUseCase
import com.ovais.quickcode.features.create.domain.CodeValidationUseCase
import com.ovais.quickcode.features.create.domain.CreateCodeUseCase
import com.ovais.quickcode.features.create.domain.DefaultCanAutoOpenUrlUseCase
import com.ovais.quickcode.features.create.domain.DefaultCanCopyToClipboardUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeDefaultColorUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeFormatUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeTypeUseCase
import com.ovais.quickcode.features.create.domain.DefaultCodeValidationUseCase
import com.ovais.quickcode.features.create.domain.DefaultCreateCodeRepository
import com.ovais.quickcode.features.create.domain.DefaultCreateCodeUseCase
import com.ovais.quickcode.features.create.domain.DefaultSaveCreatedCodeUseCase
import com.ovais.quickcode.features.create.domain.SaveCreatedCodeUseCase
import com.ovais.quickcode.features.history.data.HistoryRepository
import com.ovais.quickcode.features.history.domain.DefaultDeleteCreatedCodeUseCase
import com.ovais.quickcode.features.history.domain.DefaultGetCreatedCodesUseCase
import com.ovais.quickcode.features.history.domain.DefaultGetScannedCodesUseCase
import com.ovais.quickcode.features.history.domain.DefaultHistoryRepository
import com.ovais.quickcode.features.history.domain.DeleteCodeUseCase
import com.ovais.quickcode.features.history.domain.GetCreatedCodesUseCase
import com.ovais.quickcode.features.history.domain.GetScannedCodesUseCase
import com.ovais.quickcode.features.home.domain.CanLoginUseCase
import com.ovais.quickcode.features.home.domain.CardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultCanLoginUseCase
import com.ovais.quickcode.features.home.domain.DefaultCardItemsUseCase
import com.ovais.quickcode.features.home.domain.DefaultLoginResultUseCase
import com.ovais.quickcode.features.home.domain.LoginResultUseCase
import com.ovais.quickcode.features.on_boarding.domain.DefaultGetOnBoardingItemsUseCase
import com.ovais.quickcode.features.on_boarding.domain.DefaultUpdateOnBoardingStatusUseCase
import com.ovais.quickcode.features.on_boarding.domain.GetOnBoardingItemsUseCase
import com.ovais.quickcode.features.on_boarding.domain.UpdateOnBoardingStatusUseCase
import com.ovais.quickcode.features.scan_code.data.ScanRepository
import com.ovais.quickcode.features.scan_code.domain.CanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultCanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultScanCodeUseCase
import com.ovais.quickcode.features.scan_code.domain.DefaultScanRepository
import com.ovais.quickcode.features.scan_code.domain.ScanCodeUseCase
import com.ovais.quickcode.features.settings.domain.DefaultGetLocaleUseCase
import com.ovais.quickcode.features.settings.domain.DefaultUpdateSettingUseCase
import com.ovais.quickcode.features.settings.domain.GetLocaleUseCase
import com.ovais.quickcode.features.settings.domain.UpdateSettingUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetAboutUsUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetPrivacyPolicyUseCase
import com.ovais.quickcode.utils.usecase.DefaultGetUserInfoUseCase
import com.ovais.quickcode.utils.usecase.GetAboutUsUseCase
import com.ovais.quickcode.utils.usecase.GetPrivacyPolicyUseCase
import com.ovais.quickcode.utils.usecase.GetUserInfoUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


val factoryModule = module {
    factory {
        DefaultStartDestinationUseCase(get(), get(), get(named(BACKGROUND)))
    } bind StartDestinationUseCase::class
    factory {
        DefaultUpdateOnBoardingStatusUseCase(get(), get(), get(named(BACKGROUND)))
    } bind UpdateOnBoardingStatusUseCase::class
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
    factory {
        DefaultGetLocaleUseCase(get())
    } bind GetLocaleUseCase::class

    factory {
        DefaultCodeDefaultColorUseCase(get(), get(named(BACKGROUND)))
    } bind CodeDefaultColorUseCase::class

    factory {
        DefaultGetOnBoardingItemsUseCase()
    } bind GetOnBoardingItemsUseCase::class

    factory {
        DefaultCanCopyToClipboardUseCase(get(), get(named(BACKGROUND)))
    } bind CanCopyToClipboardUseCase::class

    factory {
        DefaultCanAutoOpenUrlUseCase(get(), get(named(BACKGROUND)))
    } bind CanAutoOpenUrlUseCase::class

    factory { DefaultHistoryRepository(get()) } bind HistoryRepository::class
    factory { DefaultGetCreatedCodesUseCase(get()) } bind GetCreatedCodesUseCase::class
    factory { DefaultGetScannedCodesUseCase(get()) } bind GetScannedCodesUseCase::class
    factory { DefaultSaveCreatedCodeUseCase(get()) } bind SaveCreatedCodeUseCase::class
    factory { DefaultSaveScannedCodeUseCase(get()) } bind SaveScannedCodeUseCase::class
    factory { DefaultDeleteCreatedCodeUseCase(get()) } bind DeleteCodeUseCase::class
}