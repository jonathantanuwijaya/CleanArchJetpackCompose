package com.example.cleanarchcrypto.domain.use_case.get_coins


import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoin
import com.example.cleanarchcrypto.domain.model.Coin
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) : IGetCoinsUseCase {
    override operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success<List<Coin>>(coins))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Coin>>("Couldn't reach server. Check your internet connection."))
        }
    }
}