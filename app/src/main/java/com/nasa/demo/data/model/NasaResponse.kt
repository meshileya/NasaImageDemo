package com.nasa.demo.data.model

import com.google.gson.annotations.SerializedName

data class MarsPhotoResponse(
    @SerializedName("photos")
    val photos: List<Photo>
)

data class Photo(
    val camera: Camera,
    @SerializedName("earth_date")
    val earthDate: String,
    val id: Int,
    @SerializedName("img_src")
    val imgSrc: String,
    val rover: Rover,
    val sol: Int
)

data class Camera(
    val full_name: String,
    val id: Int,
    val name: String,
    val rover_id: Int
)

data class Rover(
    val cameras: List<CameraX>,
    val id: Int,
    val landing_date: String,
    val launch_date: String,
    val max_date: String,
    val max_sol: Int,
    val name: String,
    val status: String,
    val total_photos: Int
)

data class CameraX(
    val full_name: String,
    val name: String
)