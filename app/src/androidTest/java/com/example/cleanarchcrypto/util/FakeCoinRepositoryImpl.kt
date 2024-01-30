package com.example.cleanarchcrypto.util

import com.example.cleanarchcrypto.FakeObjects
import com.example.cleanarchcrypto.data.remote.dto.CoinDetailDto
import com.example.cleanarchcrypto.data.remote.dto.CoinDto
import com.example.cleanarchcrypto.domain.repository.CoinRepository

class FakeCoinRepository : CoinRepository {

    override suspend fun getCoins(): List<CoinDto> {
        // Generate and return fake list of CoinDto
        return FakeObjects.mockCoinList
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        // Generate and return a fake CoinDetailDto based on the coinId
        return when (coinId) {
            "btc" -> FakeObjects.mockCoinDetail     // Add more dummy data for other coinIds as needed
            else -> throw IllegalArgumentException("Unknown coinId: $coinId")
        }
    }
}