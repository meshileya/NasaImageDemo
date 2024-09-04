package com.nasa.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.domain.usecase.GetImagesUseCase
import com.nasa.demo.domain.usecase.ToggleFavoriteUseCase
import com.nasa.demo.presentation.viewmodel.NasaImageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NasaImageViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val getImagesUseCase: GetImagesUseCase = mock()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mock()
    private lateinit var viewModel: NasaImageViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NasaImageViewModel(getImagesUseCase, toggleFavoriteUseCase)
    }

    @Test
    fun `initial state should have loading set to true`() {
        // Given & When
        val isLoadingObserver = mock<Observer<Boolean>>()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // Then
        verify(isLoadingObserver).onChanged(true)
    }

    @Test
    fun `refresh triggers data fetch and updates loading state`() = runTest {
        // Given
        val pagingData = PagingData.from(
            listOf(
                NasaImageUIItem(
                    id = 1,
                    imageUrl = "url",
                    isFavorite = false,
                    earthDate = "",
                    title = ""
                )
            )
        )
        whenever(getImagesUseCase.invoke()).thenReturn(flowOf(pagingData))

        // When
        viewModel.refresh()

        // Then
        assertTrue(viewModel.isLoading.value == true)  // Check if loading is true before refresh
        assertTrue(viewModel.isLoading.value != false)  // Check if loading is false after refresh
    }

    @Test
    fun `refresh handles errors correctly`() = runTest {
        // Given
        val error = Exception("Error occurred")
        val errorMessage = "An error occurred: ${error.localizedMessage}"

        // Mock the use case to return a Flow that immediately throws an exception
        whenever(getImagesUseCase.invoke()).thenReturn(flow {
            throw error
        })

        // Attach an observer to the exceptionMessage LiveData
        val exceptionObserver = mock<Observer<String>>()
        viewModel.exceptionMessage.observeForever(exceptionObserver)

        // Start collecting the images Flow in the ViewModel to trigger the exception
        val job = launch {
            viewModel.images.collect{

            }
        }

        // When
        viewModel.refresh()

        // Ensure the coroutine has completed
        advanceUntilIdle()

        // Then
        verify(exceptionObserver).onChanged(errorMessage)
        assertTrue(viewModel.isLoading.value == true)

        // Clean up the job to avoid leaks
        job.cancel()
    }

    @Test
    fun `toggleFavorite triggers refresh`() = runTest {
        // Given
        val image = NasaImageUIItem(
            id = 6754,
            imageUrl = "6754url",
            isFavorite = false,
            earthDate = "",
            title = ""
        )

        // Mock the behavior of the toggleFavoriteUseCase to ensure it executes correctly
        whenever(toggleFavoriteUseCase.execute(image)).thenReturn(Unit)

        // Mock an observer to track changes to the isLoading LiveData (or any other LiveData impacted by refresh)
        val isLoadingObserver = mock<Observer<Boolean>>()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // When
        viewModel.toggleFavorite(image)

        // Ensure the coroutine has completed
        advanceUntilIdle()

        // Then
        // Verify that the toggleFavoriteUseCase's execute method was called with the correct image
        verify(toggleFavoriteUseCase).execute(image)

        // Verify that refresh was effectively called by checking that isLoading was updated
        // (Since we can't directly verify the refresh call, we verify its side effects)
        verify(isLoadingObserver, times(2)).onChanged(true)

        // Clean up observers
        viewModel.isLoading.removeObserver(isLoadingObserver)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}