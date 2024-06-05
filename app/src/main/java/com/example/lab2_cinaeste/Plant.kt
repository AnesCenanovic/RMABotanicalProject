package com.example.lab2_cinaeste

import android.icu.number.ScientificNotation
import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("id") val id: Int,
    @SerializedName("family") val familyName: String,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("image_url") val image: String?
)
