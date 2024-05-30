package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    @SerializedName("data")
    private val data: List<Plant>? = null // Assuming the list contains Plant objects
) {
    // Getter for data
    fun getData(): List<Plant>? {
        return data
    }
}

