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
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.mockwebserver.MockWebServer
@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi = runBlocking(Dispatchers.IO) {
        val server = MockWebServer()
        server.start()
        Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }

}