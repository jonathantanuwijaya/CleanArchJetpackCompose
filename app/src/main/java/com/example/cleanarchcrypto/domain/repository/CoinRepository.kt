package com.example.cleanarchcrypto.domain.repository

import com.example.cleanarchcrypto.data.remote.dto.CoinDetailDto
import com.example.cleanarchcrypto.data.remote.dto.CoinDto

interface CoinRepository {
    suspend fun getCoins():List<CoinDto>
    suspend fun getCoinById(coinId:String): CoinDetailDto
}