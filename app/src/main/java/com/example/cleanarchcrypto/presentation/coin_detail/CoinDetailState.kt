package com.example.cleanarchcrypto.presentation.coin_detail

import com.example.cleanarchcrypto.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)