package com.nasa.demo.domain.usecase

import com.nasa.demo.domain.model.NasaImageUIItem


interface ToggleFavoriteUseCase {
    suspend fun execute(image: NasaImageUIItem)
}