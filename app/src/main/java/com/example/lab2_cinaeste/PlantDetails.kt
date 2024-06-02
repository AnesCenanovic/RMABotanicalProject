package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class PlantDetails (
    @SerializedName("id") val id: Int,
    @SerializedName("edible") val isEdible: Boolean,
    @SerializedName("specifications") val specifications: Specifications,
    @SerializedName("growth") val growth: Growth,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("family") val familyName: String,
)