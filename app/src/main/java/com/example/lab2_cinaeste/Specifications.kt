package com.example.lab2_cinaeste

import com.google.gson.annotations.SerializedName


data class Specifications (
    @SerializedName("toxicity") val toxic: String?
)