package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    @SerializedName("data")
    private val data: List<Plant>,
    @SerializedName("meta") val meta: pageCount,
    private val biljke: List<Biljka>
) {
    // Getter for data
    fun getData(): List<Plant> {
        return data
    }
    // Getter for biljke
    fun getBiljke(): List<Biljka> {
        return biljke
    }
}
data class pageCount(
    @SerializedName("total")
    private val total: Int
){
    fun getTotal(): Int {
        return total
    }
}


