package com.ovais.qrlab.core.di

import com.ovais.qrlab.logger.DefaultQRLogger
import com.ovais.qrlab.logger.QRLogger
import com.ovais.qrlab.utils.file.DefaultFileManager
import com.ovais.qrlab.utils.file.FileManager
import com.ovais.qrlab.utils.permissions.DefaultPermissionManager
import com.ovais.qrlab.utils.permissions.PermissionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


const val BACKGROUD = "IO"
const val UI = "Main"
const val DEFAULT = "Default"
val singletonModule = module {
    single<CoroutineDispatcher>(named(BACKGROUD)) {
        Dispatchers.IO
    }
    single<CoroutineDispatcher>(named(DEFAULT)) {
        Dispatchers.Default
    }
    single<CoroutineDispatcher>(named(UI)) {
        Dispatchers.Main
    }
    single { DefaultQRLogger() } bind QRLogger::class
    single { DefaultFileManager(get(), get(named(BACKGROUD))) } bind FileManager::class
    single { DefaultPermissionManager(get()) } bind PermissionManager::class
}
