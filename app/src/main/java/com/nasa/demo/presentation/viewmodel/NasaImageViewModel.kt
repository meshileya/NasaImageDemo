package com.nasa.demo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.domain.usecase.GetImagesUseCase
import com.nasa.demo.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class NasaImageViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    // LiveData to track loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData to handle exceptions
    private val _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    //
//        private val _refreshTrigger = MutableStateFlow<Unit>(Unit)
//    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    init {
//        _isLoading.value = true
    }


    val favoriteImages: LiveData<List<NasaImageUIItem>> = liveData {
        emitSource(
            getImagesUseCase.invokeFavorite()
                .catch { exception ->
                    _exceptionMessage.value = "Failed to load favorites: ${exception.message}"
                    emit(emptyList()) // Handle empty or fallback list
                }
                .asLiveData()
        )
    }

    private var currentQuery: String = ""

    // Combined Flow for images with refresh trigger handling
    @OptIn(ExperimentalCoroutinesApi::class)
    val images: Flow<PagingData<NasaImageUIItem>> = _refreshTrigger
        .flatMapLatest {
            getImagesUseCase.invoke()
                .map { pagingData ->
                    if (currentQuery.isNotEmpty()) {
                        pagingData.filter { item ->
                            item.id.toString().contains(currentQuery, ignoreCase = true)
                        }
                    } else {
                        pagingData
                    }
                }
                .onStart { _isLoading.postValue(true) } // Set loading state before data is fetched
                .onEach { _isLoading.postValue(false) } // Reset loading state after data is fetched
                .catch { exception ->
                    _exceptionMessage.postValue("An error occurred: ${exception.message}")
                    emit(PagingData.empty()) // Provide fallback for error state
                }.flowOn(Dispatchers.IO)
        }
        .cachedIn(viewModelScope)

    fun updateQuery(query: String) {
        currentQuery = query
        refresh() // Trigger a refresh to apply the new query
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
//            _refreshTrigger.value = Unit
            _refreshTrigger.emit(Unit)
        }
    }

    fun onError(exception: Throwable) {
        if (exception is HttpException) {
            _exceptionMessage.value = "HTTP Error: ${exception.message}"
        } else {
            _exceptionMessage.value = "An error occurred: ${exception.localizedMessage}"
        }
        _isLoading.value = false
    }

    fun toggleFavorite(image: NasaImageUIItem) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(image)
            refresh()
        }
    }
}