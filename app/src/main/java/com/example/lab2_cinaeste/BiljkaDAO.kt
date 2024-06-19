package com.example.lab2_cinaeste

import android.graphics.Bitmap
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface BiljkaDAO {

    @Insert
    suspend fun saveBiljka(biljka: Biljka) : Boolean {
        return try {
            insertBiljka(biljka)
            true
        } catch (e: Exception) {
            false
        }
    }

    @Insert
    suspend fun insertBiljka(biljka: Biljka) : Long

    @Insert
    suspend fun insertBiljkaBitmap(biljkaBitmap: BiljkaBitmap) : Long

    @Insert
    suspend fun fixOfflineBiljka(): Int {
        val trefleDAO = TrefleDAO()
        val initialUnchecked = countUncheckedBiljka()
        Log.w("fixOfflineBiljka", "$initialUnchecked")
        val uncheckedBiljke = getUncheckedBiljka()
        for(biljka in uncheckedBiljke){
            val biljkaFixed = trefleDAO.fixData(biljka)
            updateBiljka(biljkaFixed)
        }
        val afterUpdateUnchecked = countUncheckedBiljka()
        Log.w("fixOfflineBiljka", "$afterUpdateUnchecked")
        return initialUnchecked - afterUpdateUnchecked
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(idBiljke: Int, bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            val existingPlant = getBiljkaById(idBiljke)
            if (existingPlant == null) {
                Log.w("addImage", "Failed to find plant with that Id")
                return@withContext false
            }
            val existingImageForPlant = getBiljkaBitmapById(idBiljke)
            if (existingImageForPlant != null) {
                Log.w("addImage", "Plant already has image")
                return@withContext false
            }
            try {
                val biljkaBitmap = BiljkaBitmap(0, idBiljke, bitmap)
                val rez = insertBiljkaBitmap(biljkaBitmap)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    @Query("SELECT * FROM Biljka WHERE id = :idBiljke")
    suspend fun getBiljkaById(idBiljke: Int): Biljka?

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idTrazeneBiljke")
    suspend fun getBiljkaBitmapById(idTrazeneBiljke: Int): BiljkaBitmap?

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getUncheckedBiljka(): List<Biljka>

    @Query("SELECT COUNT(*) FROM Biljka WHERE onlineChecked = 0")
    suspend fun countUncheckedBiljka(): Int

    @Query("DELETE FROM Biljka")
    suspend fun clearData()

    @Query("SELECT COUNT(*) FROM Biljka")
    suspend fun getCount(): Int

    @Insert
    suspend fun insertAllBiljkas(biljke: List<Biljka>)

    @Update
    suspend fun updateBiljka(biljka: Biljka)

}