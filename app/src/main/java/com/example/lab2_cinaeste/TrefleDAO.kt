package com.example.lab2_cinaeste

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder



public class TrefleDAO {

    private val BASE_URL = "https://trefle.io/api/v1/plants/"
    private val API_KEY = "s7kpQm5p0knKhuEqpV80_MJTaqTlUNeMmdrPhtKemfs" // sakrit?

    suspend fun getImage(biljka: Biljka): Bitmap? {

        val latinName = getLatin(biljka.naziv)

        if (latinName.isEmpty()) {
            Log.w("TrefleDAO", "Failed to extract Latin name from ${biljka.naziv}")
            return null
        }

        val encodedLatinName = withContext(Dispatchers.IO) {
            URLEncoder.encode(latinName, "UTF-8")
        }

        val url = URL("https://trefle.io/api/v1/species/search?q=$encodedLatinName&token=$API_KEY")
        Log.d("TrefleDAO", "Query: ${url}")
        val connection = with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            connect()
            this
        }

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val response = withContext(Dispatchers.IO) { connection.inputStream.bufferedReader().readText() }
            val jsonObject = Gson().fromJson(response, JsonObject::class.java)
            val dataArray = jsonObject.getAsJsonArray("data")
            if (dataArray.size() > 0) {
                val plantObject = dataArray.get(0).asJsonObject
                val imageUrl = plantObject.get("image_url").asString
                if (imageUrl.isNotEmpty()) {
                    val bitmap = withContext(Dispatchers.IO) {
                        val imageConnection = URL(imageUrl).openConnection() as HttpURLConnection
                        imageConnection.requestMethod = "GET"
                        imageConnection.doInput = true
                        imageConnection.connect()
                        if (imageConnection.responseCode == HttpURLConnection.HTTP_OK) {
                            BitmapFactory.decodeStream(imageConnection.inputStream)
                        } else {
                            null
                        }
                    }
                    return bitmap
                }
            }
        }

        Log.w("TrefleDAO", "Failed to get image for ${biljka.naziv}")
        return null
    }

    public fun getLatin(naziv: String): String {
        val regex = Regex("""\((.*?)\)""")
        val match = regex.find(naziv)
        if (match != null) {
            Log.d("TrefleDAO", "Extracted Latin name: ${match.value}")
            return match.groupValues[1]
        } else {
            Log.w("TrifleDAO", "Failed to extract Latin name from $naziv")
            return ""
        }
    }


}