package com.example.cleanarchcrypto.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.domain.model.Coin
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(private val getCoinsUseCase: IGetCoinsUseCase) :
    ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            try {
                getCoinsUseCase.invoke().collect { result ->
                    handleCoinsResult(result)
                }
            } catch (e: Exception) {
                handleError("An unexpected error happened")
            }
        }
    }

    private fun handleCoinsResult(result: Resource<List<Coin>>) {
        when (result) {
            is Resource.Success -> handleSuccess(result.data ?: emptyList())
            is Resource.Error -> handleError(result.message ?: "An unexpected error happened")
            is Resource.Loading -> handleLoading()
        }
    }

    private fun handleSuccess(coins: List<Coin>) {
        _state.value = CoinListState(coins = coins)
    }

    private fun handleError(errorMessage: String) {
        _state.value = CoinListState(error = errorMessage)
    }

    private fun handleLoading() {
        _state.value = CoinListState(isLoading = true)
    }
}
