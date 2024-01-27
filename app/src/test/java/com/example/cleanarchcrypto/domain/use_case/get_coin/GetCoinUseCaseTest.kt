package com.example.cleanarchcrypto.domain.use_case.get_coin

import android.net.http.HttpException
import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoinDetail
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import com.example.cleanarchcrypto.utils.CommonObject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetCoinUseCaseTest {
    private lateinit var mockRepository: CoinRepository
    private lateinit var getCoinUseCase: GetCoinUseCase
    private val coinId = "c1"

    @Before
    fun setup() {
        mockRepository = mockk()
        getCoinUseCase = GetCoinUseCase(mockRepository)
    }

    @Test
    fun `invoke - should return loading and success`() = runBlocking {
        // Arrange

        val mockCoinDetail = CommonObject.mockCoinDetail

        coEvery { mockRepository.getCoinById(coinId) } returns mockCoinDetail

        // Act
        val result = getCoinUseCase(coinId).toList()

        // Assert emitted state
        assertEquals(2, result.size)

        // First emission should be Resource.Loading
        assertEquals(Resource.Loading::class, result[0]::class)
        assertTrue(result[1] is Resource.Success<*>)
        assertEquals(mockCoinDetail.toCoinDetail(), result[1].data)
        coVerify(exactly = 1) { mockRepository.getCoinById(coinId) }
    }

    @Test
    fun `invoke - should return loading and error when HttpException occurs`() = runBlocking {
        // Arrange
        val errorMessage = "Server error"
        coEvery { mockRepository.getCoinById(coinId) } throws HttpException(errorMessage, null)

        // Act
        val result = getCoinUseCase(coinId).toList()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading<*>)

        assertTrue(result[1] is Resource.Error)
        coVerify(exactly = 1) { mockRepository.getCoinById(coinId) }
    }

    @Test
    fun `invoke - should return loading and error when IOException occurs`() = runBlocking {
        // Arrange
        val errorMessage = "Couldn't reach server. Check your internet connection."
        coEvery { mockRepository.getCoinById(coinId) } throws IOException()

        // Act
        val result = getCoinUseCase(coinId).toList()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading<*>)

        val secondEmission = result[1]
        assertTrue(secondEmission is Resource.Error)
        assertEquals(errorMessage, secondEmission.message)
        coVerify(exactly = 1) { mockRepository.getCoinById(coinId) }
    }
}