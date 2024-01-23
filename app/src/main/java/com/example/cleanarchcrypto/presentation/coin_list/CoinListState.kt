package com.example.cleanarchcrypto.presentation.coin_list

import com.example.cleanarchcrypto.domain.model.Coin

data class CoinListState(
    val isLoading:Boolean = false,
    val coins:List<Coin> = emptyList(),
    val error:String = ""
)