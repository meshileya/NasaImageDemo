package com.nasa.demo.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nasa.demo.data.model.NasaImage
import com.nasa.demo.data.model.NasaImageEntity
import com.nasa.demo.data.remote.api.NasaApiService
import com.nasa.demo.domain.model.Result
import retrofit2.HttpException

class RemoteDataSourceImpl(private val service: NasaApiService) : RemoteDatasource {
//    override suspend fun getImages(sol: Int, page: Int): Result<List<NasaImage>> {
//        return try {
//            val response = service.getMarsPhotos(sol, page)
//            if (response.isSuccessful) {
//                val photos = response.body()?.photos.orEmpty()
//                val nasaImages = photos.map { photo ->
//                    NasaImage(
//                        id = photo.id,
//                        imageUrl = photo.img_src,
//                        earthDate = photo.earth_date,
//                        title = photo.rover.name
//                    )
//                }
//                Result.Success(nasaImages)
//            } else {
//                Result.Error(response.code(), response.message())
//            }
//        } catch (ex: Exception) {
//            when (ex) {
//                is HttpException -> Result.Error(ex.code(), ex.message())
//                else -> Result.Error(-1, ex.message ?: "Unknown error")
//            }
//        }
//    }

    override fun getImages(): PagingSource<Int, NasaImage> {
        return object : PagingSource<Int, NasaImage>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NasaImage> {
                val page = params.key ?: 1
                return try {
                    val response = service.getMarsPhotos(sol = 1000, page = page)
                    val photos = response.body()?.photos.orEmpty()
                    val nasaImages = photos.map { photo ->
                        NasaImage(
                            id = photo.id,
                            imageUrl = photo.imgSrc,
                            earthDate = photo.earthDate,
                            title = photo.rover.name
                        )
                    }
                    LoadResult.Page(
                        data = nasaImages,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (photos.isEmpty()) null else page + 1
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, NasaImage>): Int {
                return 0
            }
        }
    }
}