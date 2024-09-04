package com.nasa.demo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nasa.demo.data.local.datasource.LocalDataSource
import com.nasa.demo.data.model.NasaImage
import com.nasa.demo.data.model.NasaImageEntity
import com.nasa.demo.data.remote.datasource.RemoteDatasource
import kotlinx.coroutines.flow.Flow

class NasaImageRepositoryImpl(
    private val remoteDataSource: RemoteDatasource,
    private val localDataSource: LocalDataSource
) : NasaImageRepository {

    override suspend fun getNasaImages(): Flow<PagingData<NasaImage>> {
        return Pager(
            config = PagingConfig(pageSize = 20)
        ) {
            remoteDataSource.getImages()
        }.flow
    }

    override suspend fun toggleFavoriteImage(image: NasaImageEntity) {
        if (image.isFavorite) {
            localDataSource.saveFavoriteImage(image)
        } else {
            localDataSource.removeFavourite(image)
        }
    }

    override fun getFavoriteImages(): Flow<List<NasaImageEntity>> {
        return localDataSource.getFavoriteImages()
    }
}
