package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("id") val id: Int,
    @SerializedName("family") val familyName: String
)