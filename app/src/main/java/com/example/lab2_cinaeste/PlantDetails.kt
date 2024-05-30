package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class PlantDetails (
    @SerializedName("id") val id: Int,
    @SerializedName("edible") val isEdible: Boolean,
    @SerializedName("main_species.specifications.toxicity") val toxic: String?,
    @SerializedName("main_species.specifications.growth.soil_texture") val soilTexture: Int,
)