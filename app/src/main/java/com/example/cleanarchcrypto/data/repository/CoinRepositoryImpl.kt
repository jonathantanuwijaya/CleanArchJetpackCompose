package com.example.cleanarchcrypto.data.repository

import com.example.cleanarchcrypto.data.remote.CoinPaprikaApi
import com.example.cleanarchcrypto.data.remote.dto.CoinDetailDto
import com.example.cleanarchcrypto.data.remote.dto.CoinDto
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository {

    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}