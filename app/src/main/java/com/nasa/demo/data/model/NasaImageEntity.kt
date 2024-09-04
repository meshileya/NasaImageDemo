package com.nasa.demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nasa.demo.domain.model.NasaImageUIItem

@Entity(tableName = "images")
data class NasaImageEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    val earthDate: String,
    val isFavorite: Boolean = false
)

fun NasaImageEntity.toUIItem(isFavorite: Boolean = false): NasaImageUIItem {
    return NasaImageUIItem(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        earthDate = this.earthDate,
        isFavorite = isFavorite
    )
}