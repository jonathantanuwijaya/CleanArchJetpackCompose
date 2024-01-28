package com.example.cleanarchcrypto.data.repository

import com.example.cleanarchcrypto.data.remote.CoinPaprikaApi
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import com.example.cleanarchcrypto.utils.CommonObject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CoinRepositoryImplTest {

    private lateinit var api: CoinPaprikaApi

    @Before
    fun setup() {
        api = mockk<CoinPaprikaApi>()
    }

    @Test
    fun `getCoins should return a list of CoinDto`() = runBlocking {
        // Given
        val coinRepository: CoinRepository = CoinRepositoryImpl(api)

        val mockCoinList = CommonObject.mockCoinList
        coEvery { api.getCoins() } returns mockCoinList

        // When
        val result = coinRepository.getCoins()

        // Then
        assertEquals(mockCoinList, result)
    }

    @Test
    fun `getCoinById should return a CoinDetailDto`() = runBlocking {
        // Given
        val coinRepository: CoinRepository = CoinRepositoryImpl(api)

        val coinId = "CoinId"
        val mockCoinDetailDto = CommonObject.mockCoinDetail
        coEvery { api.getCoinById(coinId) } returns mockCoinDetailDto

        // When
        val result = coinRepository.getCoinById(coinId)

        // Then
        assertEquals(mockCoinDetailDto, result)
    }
}
