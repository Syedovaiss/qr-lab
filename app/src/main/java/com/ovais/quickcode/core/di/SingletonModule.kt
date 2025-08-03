package com.ovais.quickcode.core.di

import androidx.credentials.CredentialManager
import com.ovais.quickcode.analytics.AppAnalyticsManager
import com.ovais.quickcode.analytics.DefaultAppAnalyticsManager
import com.ovais.quickcode.auth.AuthManager
import com.ovais.quickcode.auth.DefaultAuthManager
import com.ovais.quickcode.locale.AppLocaleManager
import com.ovais.quickcode.locale.DefaultAppLocaleManager
import com.ovais.quickcode.locale.DefaultLocaleProvider
import com.ovais.quickcode.locale.LocaleProvider
import com.ovais.quickcode.logger.AppLogger
import com.ovais.quickcode.logger.DefaultAppLogger
import com.ovais.quickcode.notification.DefaultQuickCodeNotificationManager
import com.ovais.quickcode.notification.QuickCodeNotificationManager
import com.ovais.quickcode.storage.DefaultQuickCodeConfigurationManager
import com.ovais.quickcode.storage.DefaultQuickCodePreferenceManager
import com.ovais.quickcode.storage.QuickCodeConfigurationManager
import com.ovais.quickcode.storage.QuickCodePreferenceManager
import com.ovais.quickcode.storage.db.AppStorageManager
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.storage.db.DefaultAppStorageManager
import com.ovais.quickcode.utils.DefaultInitialProvider
import com.ovais.quickcode.utils.local_config.DefaultLocalConfigurationManager
import com.ovais.quickcode.utils.InitialProvider
import com.ovais.quickcode.utils.clipboard.ClipboardManager
import com.ovais.quickcode.utils.clipboard.DefaultClipboardManager
import com.ovais.quickcode.utils.local_config.LocalConfigurationManager
import com.ovais.quickcode.utils.file.DefaultFileManager
import com.ovais.quickcode.utils.file.FileManager
import com.ovais.quickcode.utils.permissions.DefaultPermissionManager
import com.ovais.quickcode.utils.permissions.PermissionManager
import com.ovais.quickcode.utils.sound.AppSoundManager
import com.ovais.quickcode.utils.sound.DefaultAppSoundManager
import com.ovais.quickcode.utils.usecase.DefaultGetTermsAndConditionsUseCase
import com.ovais.quickcode.utils.usecase.DefaultLocalConfigurationUseCase
import com.ovais.quickcode.utils.usecase.GetTermsAndConditionsUseCase
import com.ovais.quickcode.utils.usecase.LocalConfigurationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


const val BACKGROUND = "IO"
const val UI = "Main"
const val DEFAULT = "Default"
val singletonModule = module {
    single<CoroutineDispatcher>(named(BACKGROUND)) {
        Dispatchers.IO
    }
    single<CoroutineDispatcher>(named(DEFAULT)) {
        Dispatchers.Default
    }
    single<CoroutineDispatcher>(named(UI)) {
        Dispatchers.Main
    }
    single { DefaultLocalConfigurationManager() } bind LocalConfigurationManager::class

    single { DefaultClipboardManager(get()) } bind ClipboardManager::class
    single { DefaultAppSoundManager(get()) } bind AppSoundManager::class
    single { DefaultLocalConfigurationUseCase(get()) } bind LocalConfigurationUseCase::class
    single { DefaultAuthManager(CredentialManager.create(get()), get()) } bind AuthManager::class
    single { DefaultGetTermsAndConditionsUseCase(get()) } bind GetTermsAndConditionsUseCase::class
    single { DefaultAppLogger() } bind AppLogger::class
    single { DefaultFileManager(get(), get(named(BACKGROUND))) } bind FileManager::class
    single { DefaultPermissionManager(get()) } bind PermissionManager::class
    single { DefaultAppAnalyticsManager() } bind AppAnalyticsManager::class
    single { DefaultQuickCodePreferenceManager(get(named(DEFAULT))) } bind QuickCodePreferenceManager::class
    single { DefaultQuickCodeNotificationManager(get()) } bind QuickCodeNotificationManager::class
    single { DefaultQuickCodeConfigurationManager(get()) } bind QuickCodeConfigurationManager::class
    single { DefaultAppStorageManager(get(), get()) } bind AppStorageManager::class
    single { DefaultInitialProvider() } bind InitialProvider::class

    //Locale
    single { DefaultLocaleProvider() } bind LocaleProvider::class
    single { DefaultAppLocaleManager(get(), get()) } bind AppLocaleManager::class

    // Database DAO
    single { get<AppStorageManager>().instance.configDao() } bind ConfigurationDao::class
}
