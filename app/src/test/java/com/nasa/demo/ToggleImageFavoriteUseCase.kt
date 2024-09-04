package com.nasa.demo

import com.nasa.demo.data.repository.NasaImageRepository
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.domain.model.toEntity
import com.nasa.demo.domain.usecase.ToggleImageFavoriteUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ToggleImageFavoriteUseCaseImplTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: NasaImageRepository
    private lateinit var toggleFavoriteUseCase: ToggleImageFavoriteUseCaseImpl

    @Before
    fun setup() {
        // Initialize repository mock
        repository = mock()
        toggleFavoriteUseCase = ToggleImageFavoriteUseCaseImpl(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should call repository to toggle favorite image`() =
        testDispatcher.runBlockingTest {
            // Given
            val image = NasaImageUIItem(
                id = 9834,
                imageUrl = "9834url",
                isFavorite = false,
                earthDate = "",
                title = ""
            )
            val imageEntity = image.toEntity()

            // When
            toggleFavoriteUseCase.execute(image)

            // Then
            verify(repository).toggleFavoriteImage(imageEntity)
        }
}