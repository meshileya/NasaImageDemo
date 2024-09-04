package com.nasa.demo.data.model

data class NasaImage(
    val id: Int,
    val title: String = "",
    val imageUrl: String,
    val earthDate: String = "",
)