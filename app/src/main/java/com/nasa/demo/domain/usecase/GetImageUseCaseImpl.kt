package com.nasa.demo.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.nasa.demo.data.model.toUIItem
import com.nasa.demo.data.repository.NasaImageRepository
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.domain.model.toUIItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetImageUseCaseImpl(
    private val repository: NasaImageRepository
) : GetImagesUseCase {

    override suspend operator fun invoke(): Flow<PagingData<NasaImageUIItem>> {
//        delay(100000)
        val favoriteImagesFlow = repository.getFavoriteImages().map { data ->
            data.map { it.id }.toSet()
        }

        return repository.getNasaImages().map { pagingData ->
            pagingData.map { nasaImage ->
                val isFavorite = favoriteImagesFlow.firstOrNull()?.contains(nasaImage.id) ?: false
                nasaImage.toUIItem().copy(isFavorite = isFavorite)
            }
        }
    }

    override suspend fun invokeFavorite(): Flow<List<NasaImageUIItem>> {
        return repository.getFavoriteImages().map { data ->
            data.map { nasaImageEntity ->
                nasaImageEntity.toUIItem()
            }
        }
    }
}