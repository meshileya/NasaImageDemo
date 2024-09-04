package com.nasa.demo.data.remote.datasource

import androidx.paging.PagingSource
import com.nasa.demo.data.model.NasaImage

interface RemoteDatasource {
    //    suspend fun getImages(sol: Int, page: Int): Result<List<NasaImage>>
    fun getImages(): PagingSource<Int, NasaImage>
}