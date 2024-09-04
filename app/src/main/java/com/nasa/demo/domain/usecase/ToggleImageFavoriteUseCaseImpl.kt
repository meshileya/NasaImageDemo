package com.nasa.demo.domain.usecase

import com.nasa.demo.data.repository.NasaImageRepository
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.domain.model.toEntity

class ToggleImageFavoriteUseCaseImpl(private val repository: NasaImageRepository) :
    ToggleFavoriteUseCase {

    override suspend fun execute(image: NasaImageUIItem) {
        repository.toggleFavoriteImage(image.toEntity())
    }
}