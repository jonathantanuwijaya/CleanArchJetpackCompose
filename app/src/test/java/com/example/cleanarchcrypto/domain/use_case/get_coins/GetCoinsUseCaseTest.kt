import android.net.http.HttpException
import com.example.cleanarchcrypto.common.Resource
import com.example.cleanarchcrypto.data.remote.dto.toCoin
import com.example.cleanarchcrypto.domain.repository.CoinRepository
import com.example.cleanarchcrypto.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cleanarchcrypto.utils.CommonObject
import io.mockk.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException


class GetCoinsUseCaseTest {
//    inline fun <reified T> isA() = isA<T>(T::class.java)

    private lateinit var mockRepository: CoinRepository
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()
        getCoinsUseCase = GetCoinsUseCase(mockRepository)
    }

    @Test
    fun `invoke - should return loading and success`() = runBlocking {
        // Arrange
        val mockCoinList = CommonObject().mockCoinList
        coEvery { mockRepository.getCoins() } returns mockCoinList

        // Act
        val result = getCoinsUseCase().toList()

        // Assert emitted state
        assertEquals(2, result.size)

        // First emission should be Resource.Loading
        assertEquals(Resource.Loading::class, result[0]::class)
        assertTrue(result[1] is Resource.Success<*>)
        assertEquals(mockCoinList.map { it.toCoin() }, result[1].data)
        coVerify(exactly = 1) { mockRepository.getCoins() }
    }

    @Test
    fun `invoke - should return loading and success with empty list`() = runBlocking {
        // Arrange
        coEvery { mockRepository.getCoins() } returns emptyList()

        // Act
        val result = getCoinsUseCase().toList()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading<*>)

        val secondEmission = result[1]
        assertTrue(secondEmission is Resource.Success)
        assertTrue(secondEmission.data.isNullOrEmpty())
        coVerify(exactly = 1) { mockRepository.getCoins() }
    }

    @Test
    fun `invoke - should return loading and error when HttpException occurs`() = runBlocking {
        // Arrange
        val errorMessage = "Server error"
        coEvery { mockRepository.getCoins() } throws HttpException(errorMessage, null)

        // Act
        val result = getCoinsUseCase().toList()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading<*>)

        assertTrue(result[1] is Resource.Error)
        coVerify(exactly = 1) { mockRepository.getCoins() }
    }

    @Test
    fun `invoke - should return loading and error when IOException occurs`() = runBlocking {
        // Arrange
        val errorMessage = "Couldn't reach server. Check your internet connection."
        coEvery { mockRepository.getCoins() } throws IOException()

        // Act
        val result = getCoinsUseCase().toList()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading<*>)

        val secondEmission = result[1]
        assertTrue(secondEmission is Resource.Error)
        assertEquals(errorMessage, secondEmission.message)
        coVerify(exactly = 1) { mockRepository.getCoins() }
    }
}