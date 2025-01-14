package com.connectdeaf.di

import com.connectdeaf.data.repository.FaqRepository
import com.connectdeaf.domain.usecase.SendFaqMessageUseCase
import com.connectdeaf.network.FAQApiService
import com.connectdeaf.network.RetrofitInstance
import com.connectdeaf.viewmodel.FAQViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FAQApiService> {
        RetrofitInstance.aiApi
    }
    single { FaqRepository(get()) }
    single { SendFaqMessageUseCase(get()) }
    viewModel { FAQViewModel(get()) }
}

