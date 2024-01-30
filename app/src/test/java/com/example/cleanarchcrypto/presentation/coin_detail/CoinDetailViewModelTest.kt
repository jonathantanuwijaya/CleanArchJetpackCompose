package com.example.cleanarchcrypto.presentation.coin_detail

import androidx.lifecycle.SavedStateHandle
import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoinDetail
import com.example.cleanarchcrypto.domain.model.CoinDetail
import com.example.cleanarchcrypto.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinUseCase
import com.example.cleanarchcrypto.utils.CommonObject
import com.example.cleanarchcrypto.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CoinDetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var mockGetCoinUseCase: GetCoinUseCase

    @Before
    fun setUp() {
        mockGetCoinUseCase = mockk()
        savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    }

    @Test
    fun `given GetCoinUseCase should returns loading with loading state`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val coinId = "sample_id"
            val flowQuestions =
                flowOf(Resource.Loading<CoinDetail>())
            coEvery { savedStateHandle.get<String>(any()) } returns coinId
            coEvery { mockGetCoinUseCase(any()) } returns flowQuestions

            // When
            viewModel = CoinDetailViewModel(mockGetCoinUseCase, savedStateHandle)

            // Then
            val state = viewModel.state.value
            assertEquals(null, state.coin)
            assertEquals(true, state.isLoading)
            assertTrue(state.error.isEmpty())
            coVerify(exactly = 1) { mockGetCoinUseCase(any()) }
        }

    @Test
    fun `given GetCoinUseCase should returns success Coin Detail `() =
        runTest {
            // Given
            val mockCoinDetail = CommonObject.mockCoinDetail.toCoinDetail()
            val coinId = "sample_id"
            val flowQuestions =
                flowOf(Resource.Success<CoinDetail>(mockCoinDetail))
            coEvery { savedStateHandle.get<String>(any()) } returns coinId
            coEvery { mockGetCoinUseCase(any()) } returns flowQuestions

            // When
            viewModel = CoinDetailViewModel(mockGetCoinUseCase, savedStateHandle)


            // Then
            val state = viewModel.state.value
            assertEquals(mockCoinDetail.coinId, state.coin?.coinId ?: "")
            assertEquals(mockCoinDetail, state.coin)
            assertEquals(false, state.isLoading)
            assertTrue(state.error.isEmpty())
            coVerify(exactly = 1) { mockGetCoinUseCase(any()) }
        }

    @Test
    fun `given GetCoinsUseCase should returns error with error message`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val errorMsg = "data error"
            val coinId = "sample_id"
            val flowQuestions =
                flowOf(Resource.Error<CoinDetail>(errorMsg))
            coEvery { savedStateHandle.get<String>(any()) } returns coinId
            coEvery { mockGetCoinUseCase(any()) } returns flowQuestions

            // When
            viewModel = CoinDetailViewModel(mockGetCoinUseCase, savedStateHandle)
            // Then
            val state = viewModel.state.value
            assertEquals(null, state.coin?.coinId)
            assertEquals(false, state.isLoading)
            assertEquals(errorMsg, state.error)
            coVerify(exactly = 1) { mockGetCoinUseCase(any()) }
        }

}