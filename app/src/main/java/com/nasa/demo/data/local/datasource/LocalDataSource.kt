package com.nasa.demo.data.local.datasource

import com.nasa.demo.data.local.dao.NasaImageDao
import com.nasa.demo.data.model.NasaImageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val imageDao: NasaImageDao
) {

    suspend fun saveFavoriteImage(image: NasaImageEntity) {
        imageDao.insertImage(image)
    }

    suspend fun removeFavourite(image: NasaImageEntity) {
        imageDao.removeFavourite(image)
    }

    fun getFavoriteImages(): Flow<List<NasaImageEntity>> {
        return imageDao.getFavoriteImages()
    }
}