package com.nasa.demo.di

import android.content.Context
import androidx.room.Room
import com.nasa.demo.BuildConfig
import com.nasa.demo.common.preference.DatabaseKeys
import com.nasa.demo.data.local.dao.NasaImageDao
import com.nasa.demo.data.local.database.AppDatabase
import com.nasa.demo.data.local.datasource.LocalDataSource
import com.nasa.demo.data.remote.api.NasaApiService
import com.nasa.demo.data.remote.datasource.RemoteDataSourceImpl
import com.nasa.demo.data.remote.datasource.RemoteDatasource
import com.nasa.demo.data.repository.NasaImageRepository
import com.nasa.demo.data.repository.NasaImageRepositoryImpl
import com.nasa.demo.domain.usecase.GetImageUseCaseImpl
import com.nasa.demo.domain.usecase.GetImagesUseCase
import com.nasa.demo.domain.usecase.ToggleFavoriteUseCase
import com.nasa.demo.domain.usecase.ToggleImageFavoriteUseCaseImpl
import com.nasa.demo.presentation.viewmodel.NasaImageViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpLogginInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpLoggingInterceptor: HttpLoggingInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(BuildConfig.TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(BuildConfig.TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(BuildConfig.TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    @Provides
    @Singleton
    fun provideNasaApiService(retrofit: Retrofit): NasaApiService {
        return retrofit.create(NasaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteSource(service: NasaApiService): RemoteDatasource {
        return RemoteDataSourceImpl(service)
    }


    @Provides
    fun provideNasaImageRepository(
        remoteDataSource: RemoteDatasource,
        localDataSource: LocalDataSource
    ): NasaImageRepository {
        return NasaImageRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideGetNasaImagesUseCase(repository: NasaImageRepository): GetImagesUseCase {
        return GetImageUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideToggleImageFavoriteUseCase(repository: NasaImageRepository): ToggleFavoriteUseCase {
        return ToggleImageFavoriteUseCaseImpl(repository)
    }


    @Provides
    @Singleton
    fun providesNasaImageViewModel(
        getImagesUseCase: GetImagesUseCase,
        getToggleFavoriteUseCase: ToggleFavoriteUseCase
    ): NasaImageViewModel {
        return NasaImageViewModel(getImagesUseCase, getToggleFavoriteUseCase)
    }

    @Provides
    @Singleton
    fun provideNasaImageDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DatabaseKeys.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun providesNasaImageDao(database: AppDatabase): NasaImageDao =
        database.nasaImageDao()

}