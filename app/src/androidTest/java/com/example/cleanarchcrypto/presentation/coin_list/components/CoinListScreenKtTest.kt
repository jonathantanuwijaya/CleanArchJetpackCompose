package com.example.cleanarchcrypto.presentation.coin_list.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.cleanarchcrypto.FakeObjects
import com.example.cleanarchcrypto.di.AppModule
import com.example.cleanarchcrypto.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchcrypto.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cleanarchcrypto.presentation.MainActivity
import com.example.cleanarchcrypto.presentation.Screen
import com.example.cleanarchcrypto.presentation.coin_detail.CoinDetailViewModel
import com.example.cleanarchcrypto.presentation.coin_detail.components.CoinDetailScreen
import com.example.cleanarchcrypto.presentation.coin_list.CoinListViewModel
import com.example.cleanarchcrypto.presentation.theme.CleanArchCryptoTheme
import com.example.cleanarchcrypto.util.FakeCoinRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@UninstallModules(AppModule::class)
@HiltAndroidTest
class CoinListScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
    private lateinit var navController: NavHostController
    private lateinit var fakeCoinRepository: FakeCoinRepository
    private lateinit var getCoinsUseCase: GetCoinsUseCase
    private lateinit var getCoinUseCase: GetCoinUseCase
    private lateinit var viewModel: CoinListViewModel
    private lateinit var detailViewModel: CoinDetailViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        fakeCoinRepository = FakeCoinRepository()
        val savedStateHandle = SavedStateHandle(mapOf("coinId" to "btc"))
        fakeCoinRepository = FakeCoinRepository()
        getCoinUseCase = GetCoinUseCase(fakeCoinRepository)
        detailViewModel = CoinDetailViewModel(getCoinUseCase, savedStateHandle)
        getCoinsUseCase = GetCoinsUseCase(fakeCoinRepository)
        viewModel = CoinListViewModel(getCoinsUseCase = getCoinsUseCase)
        composeRule.activity.setContent {
            navController = rememberNavController()
            CleanArchCryptoTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.CoinListScreen.route
                ) {
                    composable(route = Screen.CoinListScreen.route) {
                        CoinListScreen(navController, viewModel = viewModel)
                    }
                    composable(route = Screen.CoinDetailScreen.route + "/{coinId}") {
                        CoinDetailScreen(detailViewModel)
                    }
                }
            }
        }
    }

    @Test
    fun coin_list_is_visible() {
        composeRule.onNodeWithTag("lazy").assertExists()
        composeRule.onNodeWithText("${FakeObjects.mockCoinList[0].rank}. metc (${FakeObjects.mockCoinList[0].symbol})").assertIsDisplayed()
    }

    @Test
    fun click_and_show_coin_detail() {
        composeRule.onNodeWithText("${FakeObjects.mockCoinList[0].rank}. metc (${FakeObjects.mockCoinList[0].symbol})").assertIsDisplayed()
        composeRule
            .onNodeWithText("${FakeObjects.mockCoinList[0].rank}. metc (${FakeObjects.mockCoinList[0].symbol})")
            .performClick()
        println("route now ${navController
            .currentDestination
            ?.route}")
        navController
            .currentDestination
            ?.route
            ?.startsWith(Screen.CoinDetailScreen.route)?.let { assertTrue(it) }
        composeRule.onNodeWithTag("lazydetail").assertIsDisplayed()
        composeRule
            .onNodeWithTag("coin_detail_title")
            .assertIsDisplayed()
    }
}