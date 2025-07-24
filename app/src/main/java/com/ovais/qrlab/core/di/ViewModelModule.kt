package com.ovais.qrlab.core.di

import com.ovais.qrlab.features.create.presentation.CreateCodeViewModel
import com.ovais.qrlab.features.home.presentation.HomeViewModel
import com.ovais.qrlab.features.scan_qr.presentation.ScanViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel {
        CreateCodeViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        ScanViewModel(
            get(),
            get(),
            get(named(UI))
        )
    }
}
