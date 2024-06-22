package com.example.lab2_cinaeste

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: BiljkaDatabase
    private lateinit var biljkaDao: BiljkaDAO

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeBiljkaAndReadInList() {
        val biljka = Biljka(
            0,
            "Test Plant",
            "lol",
            "lol",
            listOf(MedicinskaKorist.SMIRENJE),
            ProfilOkusaBiljke.AROMATICNO,
            listOf("lol"),
            listOf(KlimatskiTip.TROPSKA),
            listOf(Zemljiste.KRECNJACKO),
            false
        )
        runBlocking {
            biljkaDao.saveBiljka(biljka)
            val biljke = biljkaDao.getAllBiljkas()
            assertTrue("T1 - should contain ${biljka.naziv}", biljke.find { biljka -> biljka.naziv.contains("Test",ignoreCase = true) }!=null)
        }

    }

    @Test
    fun clearDataReturnEmptyList() {
        val biljka = Biljka(
            0,
            "Test Plant",
            "lol",
            "lol",
            listOf(MedicinskaKorist.SMIRENJE),
            ProfilOkusaBiljke.AROMATICNO,
            listOf("lol"),
            listOf(KlimatskiTip.TROPSKA),
            listOf(Zemljiste.KRECNJACKO),
            false
        )
        runBlocking {
            biljkaDao.saveBiljka(biljka)
            val idBiljke = biljkaDao.getAllBiljkas()[0].id
            val bitmap = Bitmap.createBitmap(200, 300, Bitmap.Config.ARGB_8888)
            biljkaDao.addImage(idBiljke ?: 0,bitmap)
            var biljke = biljkaDao.getAllBiljkas()
            assertTrue("Plant list should not be empty",biljke.isNotEmpty())
            biljkaDao.clearData()
            biljke = biljkaDao.getAllBiljkas()
            assertTrue("The plant list should",biljke.isEmpty())
        }
    }
}