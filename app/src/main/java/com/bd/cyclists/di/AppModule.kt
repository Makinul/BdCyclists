package com.bd.cyclists.di

import com.bd.cyclists.MainViewModel
import com.bd.cyclists.data.repository.MyRepository
import com.bd.cyclists.data.service.ApiService
import com.bd.cyclists.data.service.ApiServiceImpl
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel

val httpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

val appModule = module {
    // Ktor HTTP Client as a singleton
    single { httpClient }

    // API Service as a singleton, injecting the HttpClient
    single<ApiService> { ApiServiceImpl(get()) }

    // Repository as a singleton, injecting the ApiService
    single { MyRepository(get()) }

    // ViewModel, injecting the Repository
    viewModel { MainViewModel(get()) }

    // You can add other dependencies here:
    // factory { SomeUseCase(get()) }
    // single { SomeDatabase(androidContext()) } // if you have a database
}