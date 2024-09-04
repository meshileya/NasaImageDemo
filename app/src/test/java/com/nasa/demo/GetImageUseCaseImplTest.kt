package com.nasa.demo

import app.cash.turbine.test
import com.nasa.demo.data.model.NasaImageEntity
import com.nasa.demo.data.repository.NasaImageRepository
import com.nasa.demo.domain.usecase.GetImageUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetImageUseCaseImplTest {

    private lateinit var getImageUseCase: GetImageUseCaseImpl
    private val repository: NasaImageRepository = mock(NasaImageRepository::class.java)

    @Before
    fun setup() {
        getImageUseCase = GetImageUseCaseImpl(repository)
    }

    @Test
    fun `invokeFavorite returns correct favorite images`() = runTest {
        // Given
        val nasaImageEntity1 = NasaImageEntity(id = 4567)
        val nasaImageEntity2 = NasaImageEntity(id = 4568)
        val favoriteImages = listOf(nasaImageEntity1, nasaImageEntity2)

        val favoriteImagesFlow = flowOf(favoriteImages)
        whenever(repository.getFavoriteImages()).thenReturn(favoriteImagesFlow)

        // When
        getImageUseCase.invokeFavorite().test {
            val items = awaitItem()

            // Then
            assertEquals(2, items.size)
            assertTrue(items.any { it.id == 4567 })
            assertTrue(items.any { it.id == 4568 })

            awaitComplete()
        }
    }
}
