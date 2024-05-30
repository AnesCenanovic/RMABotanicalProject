package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class PlantDetailsResponse(
    @SerializedName("data")
    private val data: PlantDetails
) {
    // Getter for data
    fun getData(): PlantDetails {
        return data
    }
}