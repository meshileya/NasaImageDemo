package com.nasa.demo.domain.usecase

import androidx.paging.PagingData
import com.nasa.demo.domain.model.NasaImageUIItem
import kotlinx.coroutines.flow.Flow


interface GetImagesUseCase {
    suspend fun invoke(): Flow<PagingData<NasaImageUIItem>>
    suspend fun invokeFavorite(): Flow<List<NasaImageUIItem>>
}
