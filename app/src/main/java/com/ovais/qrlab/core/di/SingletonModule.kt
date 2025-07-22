package com.ovais.qrlab.core.di

import com.ovais.qrlab.logger.DefaultQRLogger
import com.ovais.qrlab.logger.QRLogger
import com.ovais.qrlab.utils.file.DefaultFileManager
import com.ovais.qrlab.utils.file.FileManager
import com.ovais.qrlab.utils.permissions.DefaultPermissionManager
import com.ovais.qrlab.utils.permissions.PermissionManager
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
    single { DefaultFileManager(get(), get()) } bind FileManager::class
    single { DefaultPermissionManager(get()) } bind PermissionManager::class
}
