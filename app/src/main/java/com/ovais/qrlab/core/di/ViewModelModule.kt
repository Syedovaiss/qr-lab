package com.ovais.qrlab.core.di

import com.ovais.qrlab.features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
}
