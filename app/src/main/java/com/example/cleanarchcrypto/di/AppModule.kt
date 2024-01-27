package com.example.cleanarchcrypto.di

import com.example.cleanarchcrypto.common.Constants
import com.example.cleanarchcrypto.data.remote.CoinPaprikaApi
import com.example.cleanarchcrypto.data.repository.CoinRepositoryImpl
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import com.example.cleanarchcrypto.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchcrypto.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinUseCase
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providesGetCoinUseCase(repository: CoinRepository): IGetCoinsUseCase {
        return GetCoinsUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesGetCoinsUseCase(repository: CoinRepository): IGetCoinUseCase {
        return GetCoinUseCase(repository)
    }
}