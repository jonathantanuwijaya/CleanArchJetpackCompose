package com.example.cleanarchcrypto.presentation.coin_list

import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoin
import com.example.cleanarchcrypto.domain.model.Coin
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinsUseCase
import com.example.cleanarchcrypto.utils.CommonObject
import com.example.cleanarchcrypto.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.*


@ExperimentalCoroutinesApi
class CoinListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CoinListViewModel
    private lateinit var mockGetCoinsUseCase: IGetCoinsUseCase

    @Before
    fun setUp() {
        mockGetCoinsUseCase = mockk()

    }

    @Test
    fun `given GetCoinsUseCase returns success, when getCoins is called, then return empty list`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val flowQuestions = flowOf(Resource.Success<List<Coin>>(emptyList()))
            coEvery { mockGetCoinsUseCase.invoke() } returns flowQuestions
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // When
            viewModel.getCoins()

            // Then
            val state = viewModel.state.value
            assertEquals(emptyList<Coin>(), state.coins)
        }

    @Test
    fun `given GetCoinsUseCase returns success, when getCoins is called, then update state with coins`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val mockCoins = CommonObject.mockCoinList.map { it -> it.toCoin() }
            val flowQuestions = flowOf(Resource.Success<List<Coin>>(mockCoins))
            coEvery { mockGetCoinsUseCase.invoke() } returns flowQuestions
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // When
            viewModel.getCoins()

            // Then
            val state = viewModel.state.value
            assertEquals(mockCoins, state.coins)
        }
}
