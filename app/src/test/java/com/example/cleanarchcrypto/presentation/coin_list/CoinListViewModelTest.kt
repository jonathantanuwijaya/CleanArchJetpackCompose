package com.example.cleanarchcrypto.presentation.coin_list

import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoin
import com.example.cleanarchcrypto.domain.model.Coin
import com.example.cleanarchcrypto.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cleanarchcrypto.domain.use_case.interfaces.IGetCoinsUseCase
import com.example.cleanarchcrypto.utils.CommonObject
import com.example.cleanarchcrypto.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.*
import org.junit.Assert.assertTrue


@ExperimentalCoroutinesApi
class CoinListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CoinListViewModel
    private lateinit var mockGetCoinsUseCase: GetCoinsUseCase

    @Before
    fun setUp() {
        mockGetCoinsUseCase = mockk()
    }

    @Test
    fun `given GetCoinsUseCase should returns success with empty list`() =
        runTest {
            // Given
            val flowQuestions = flowOf(Resource.Success<List<Coin>>(emptyList()))
            coEvery { mockGetCoinsUseCase() } returns flowQuestions

            // When
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // Then
            val state = viewModel.state.value
            assertEquals(emptyList<Coin>(), state.coins)
            assertEquals(false, state.isLoading)
            assertTrue(state.error.isEmpty())
            coVerify(exactly = 1) { mockGetCoinsUseCase() }
        }

    @Test
    fun `given GetCoinsUseCase should returns success with list of coins`() =
        runTest {
            // Given
            val mockCoins = CommonObject.mockCoinList.map { it -> it.toCoin() }
            val flowQuestions = flowOf(Resource.Success<List<Coin>>(mockCoins))
            coEvery { mockGetCoinsUseCase.invoke() } returns flowQuestions

            // When
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // Then
            val state = viewModel.state.value
            assertEquals(mockCoins, state.coins)
            assertEquals(false, state.isLoading)
            assertTrue(state.error.isEmpty())
            coVerify(exactly = 1) { mockGetCoinsUseCase() }
        }

    @Test
    fun `given GetCoinsUseCase should returns loading with loading state`() =
        runTest{
            // Given
            val flowQuestions = flowOf(Resource.Loading<List<Coin>>())
            coEvery { mockGetCoinsUseCase.invoke() } returns flowQuestions
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // Then
            val state = viewModel.state.value
            assertEquals(emptyList<Coin>(), state.coins)
            assertEquals(true, state.isLoading)
            assertTrue(state.error.isEmpty())
            coVerify(exactly = 1) { mockGetCoinsUseCase() }
        }

    @Test
    fun `given GetCoinsUseCase should returns error with error message`() =
        runTest {
            // Given
            val errorMsg = "data error"
            val flowQuestions = flowOf(Resource.Error<List<Coin>>(errorMsg))
            coEvery { mockGetCoinsUseCase.invoke() } returns flowQuestions

            // When
            viewModel = CoinListViewModel(mockGetCoinsUseCase)

            // Then
            val state = viewModel.state.value
            assertEquals(errorMsg, state.error)
            assertEquals(emptyList<Coin>(), state.coins)
            assertEquals(false, state.isLoading)
            coVerify(exactly = 1) { mockGetCoinsUseCase() }
        }
}
