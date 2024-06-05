package com.example.lab2_cinaeste

import android.content.Context
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


public class TrefleDAO () {

    private val API_KEY = "s7kpQm5p0knKhuEqpV80_MJTaqTlUNeMmdrPhtKemfs" // sakrit?
    private var defaultBitmap : Bitmap? = null
    private lateinit var context: Context // initialize with lateinit

    fun setContext(context: Context) {
        defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.placeholder)
    }

    private val appContext: Context
        get() = context.applicationContext


    suspend fun getImage(biljka: Biljka): Bitmap? {

        val latinName = getLatin(biljka.naziv)

        if (latinName.isEmpty()) {
            Log.w("TrefleDAO", "Failed to extract Latin name from ${biljka.naziv}")
            return defaultBitmap
        }

        val encodedLatinName = withContext(Dispatchers.IO) {
            URLEncoder.encode(latinName, "UTF-8")
        }

        val url = URL("https://trefle.io/api/v1/species/search?q=$encodedLatinName&token=$API_KEY")
        Log.d("TrefleDAO", "Query: ${url}")
        val connection = with(withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection) {
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
        return defaultBitmap
    }


    suspend fun fixData(biljka: Biljka): Biljka {
        val latinName = getLatin(biljka.naziv)
        if (latinName.isEmpty()) {
           // Log.w("TrefleDAO", "Failed to extract Latin name from ${biljka.naziv}")
            return biljka
        }

        try {
            val response = TrefleService.RetrofitClient.trefleService.searchPlants(latinName, API_KEY)
            val plant = response.getData()[0]
            val response2 = TrefleService.RetrofitClient.trefleService.getPlantDetails(plant.id,API_KEY)
            val plantDetails = response2.getData()

            if (plant != null) {
                if (plant.familyName != biljka.porodica) {
                    biljka.porodica = plant.familyName
                    //Log.w("TrefleDAO", "Updating family to ${plant.familyName}")
                    //Log.w("TrefleDAO", "New family name  ${biljka.porodica}")
                    //Log.w("TrefleDAO", "id  ${plant.id}")
                    //Log.w("TrefleDAO", "id  ${plantDetails.id}")
                }
            }
            if (!plantDetails.isEdible) {
                biljka.jela = emptyList()
                if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                    biljka.medicinskoUpozorenje += " NIJE JESTIVO"
                }
            }
            if (plantDetails.specifications.toxic != null) {
                //Log.w("TrefleDAO", "Toxicity ${plantDetails.specifications.toxic}")
                if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                    biljka.medicinskoUpozorenje += " TOKSIČNO"
                }
            }
            val tipZemljista = plantDetails.growth.soilTexture?.let { mapSoilType(it) }
            if (tipZemljista != null) {
                biljka.zemljisniTipovi = listOf(tipZemljista)
            } else {
                biljka.zemljisniTipovi = emptyList()
                //Log.w("TrefleDAO", "Unrecognized soil texture value: ${plantDetails.growth.soilTexture} for biljka: ${biljka.naziv}")
            }

            val light = plantDetails.growth.light
            val humidity = plantDetails.growth.atmosphericHumidity

            //Log.w("TrefleDAO", "Light, humidity: $light  $humidity for biljka: ${plantDetails.id}")
            if(light!=null && humidity !=null){
                val possibleClimates = mapClimateType(light,humidity)
                biljka.klimatskiTipovi = possibleClimates.toList()

                for (klima in biljka.klimatskiTipovi){
                    //Log.w("TrefleDAOClimate", "Climate for fixed plant : $klima")
                }
            }

            return biljka
        } catch (e: Exception) {
            //Log.e("TrefleDAO", "Error fetching data for ${biljka.naziv}", e)
            return biljka // Return unmodified biljka on error
        }
    }

   suspend fun getPlantsWithFlowerColor(flowerColor: String, substr : String): List<Biljka> {
       return try {
           val biljke = mutableListOf<Biljka>()
           var page = 1
           try {
               while (true) {
                   //Log.w("TrefleDAO", "page : $page")
                   val response =
                       TrefleService.RetrofitClient.trefleService.getPlantsWithFlowerColor(
                           flowerColor,
                           API_KEY,
                           page
                       )
                   page++
                   if (response.getData().isNotEmpty()) {
                       for (plant in response.getData()) {
                           //Log.w("TrefleDAO", "plant id : ${plant.id}")
                           if (plant.scientificName.contains(
                                   substr,
                                   ignoreCase = true
                               ) || plant.commonName?.contains(substr, ignoreCase = true) == true
                           ) {
                               val responsePlant =
                                   TrefleService.RetrofitClient.trefleService.getPlantDetails(
                                       plant.id,
                                       API_KEY
                                   ).getData()
                               var biljkaZaDodati = Biljka(
                                   naziv = responsePlant.commonName + "(" + responsePlant.scientificName + ")",
                                   porodica = responsePlant.familyName,
                                   medicinskoUpozorenje = "NEMA",
                                   medicinskeKoristi = listOf(MedicinskaKorist.NULL),
                                   profilOkusa = ProfilOkusaBiljke.NULL,
                                   jela = emptyList(),
                                   klimatskiTipovi = emptyList(),
                                   zemljisniTipovi = emptyList()
                               )
                               if (responsePlant.flower.color[0] != null) {
                                   biljkaZaDodati = fixData(biljkaZaDodati)
                                   biljke.add(biljkaZaDodati)
                               }
                           }
                       }
                   }
               }
           } catch (e: Exception) {
               //Log.e("TrefleDAO", "Total pages: ${page-1}", )
           }
           biljke
       }
           catch(e: Exception) {
               //Log.e("TrefleDAO", "Error fetching data ${e.message}")
               emptyList()
           }
   }
    private fun mapClimateType(light: Int, humidity: Int): List<KlimatskiTip> {

        val possibleTypes = mutableListOf<KlimatskiTip>()

            if(light in 7..9 && humidity in 1..2){
                possibleTypes.add(KlimatskiTip.SUHA)
            }
            if(light in 6..9 && humidity in 1..5){
                possibleTypes.add(KlimatskiTip.SREDOZEMNA)
            }
            if(light in 8..10 && humidity in 7..10){
            possibleTypes.add(KlimatskiTip.TROPSKA)
            }
            if(light in 6..9 && humidity in 5..8){
            possibleTypes.add(KlimatskiTip.SUBTROPSKA)
            }
            if(light in 4..7 && humidity in 3..7){
            possibleTypes.add(KlimatskiTip.UMJERENA)
            }
            if(light in 0..5 && humidity in 3..7){
            possibleTypes.add(KlimatskiTip.PLANINSKA)
            }

        return possibleTypes.toList()
    }
    private fun mapSoilType(trefleSoilTexture: Int): Zemljiste? {
        return when (trefleSoilTexture) {
            1, 2 -> Zemljiste.GLINENO
            3, 4 -> Zemljiste.PJESKOVITO
            5, 6 -> Zemljiste.ILOVACA
            7, 8 -> Zemljiste.CRNICA
            9 -> Zemljiste.SLJUNOVITO
            10 -> Zemljiste.KRECNJACKO
            else -> null
        }
    }

    private fun getLatin(naziv: String): String {
        val regex = Regex("""\((.*?)\)""")
        val match = regex.find(naziv)
        return if (match != null) {
            //Log.d("TrefleDAO", "Extracted Latin name: ${match.value}")
            match.groupValues[1]
        } else {
            //Log.w("TrifleDAO", "Failed to extract Latin name from $naziv")
            ""
        }
    }


}