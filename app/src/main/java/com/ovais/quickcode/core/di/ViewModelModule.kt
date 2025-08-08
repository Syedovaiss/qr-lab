package com.ovais.quickcode.core.di

import com.ovais.quickcode.features.code_details.presentation.BarcodeDetailsViewModel
import com.ovais.quickcode.features.create.presentation.CreateCodeViewModel
import com.ovais.quickcode.features.history.presentation.HistoryViewModel
import com.ovais.quickcode.features.home.presentation.HomeViewModel
import com.ovais.quickcode.features.on_boarding.presentation.OnBoardingViewModel
import com.ovais.quickcode.features.scan_code.presentation.ScanViewModel
import com.ovais.quickcode.features.settings.presentation.SettingViewModel
import com.ovais.quickcode.features.splash.presentation.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SplashViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel {
        HomeViewModel(
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
        CreateCodeViewModel(
            get(),
            get(),
            get(),
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
        SettingViewModel(
            get(),
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
        BarcodeDetailsViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        OnBoardingViewModel(
            get(),
            get()
        )
    }
    viewModel {
        HistoryViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
