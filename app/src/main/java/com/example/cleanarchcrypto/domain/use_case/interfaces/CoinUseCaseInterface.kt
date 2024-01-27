package com.example.cleanarchcrypto.domain.use_case.interfaces

import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.domain.model.Coin
import com.example.cleanarchcrypto.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface IGetCoinsUseCase {
     operator fun invoke(): Flow<Resource<List<Coin>>>
}

interface IGetCoinUseCase {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>>
}