package com.nasa.demo.domain.model

import com.nasa.demo.data.model.NasaImage
import com.nasa.demo.data.model.NasaImageEntity

data class NasaImageUIItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val earthDate: String,
    val isFavorite: Boolean
)


fun NasaImage.toUIItem(isFavorite: Boolean = false): NasaImageUIItem {
    return NasaImageUIItem(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        earthDate = this.earthDate,
        isFavorite = isFavorite
    )
}

fun NasaImageUIItem.toEntity(): NasaImageEntity {
    return NasaImageEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        earthDate = this.earthDate,
        isFavorite = this.isFavorite
    )
}