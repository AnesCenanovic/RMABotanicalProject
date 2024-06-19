package com.example.lab2_cinaeste

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.Locale

class NovaBiljkaActivity : AppCompatActivity() {

    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var jeloET: EditText
    private lateinit var medicinskaKoristLV: ListView
    private lateinit var medicinskaKoristLVET: EditText
    private lateinit var klimatskiTipLV: ListView
    private lateinit var klimatskiTipLVET: EditText
    private lateinit var zemljisniTipLV: ListView
    private lateinit var zemljsniTipLVET: EditText
    private lateinit var profilOkusaLV: ListView
    private lateinit var profilOkusaLVET: EditText
    private lateinit var jelaLV: ListView
    private lateinit var dodajJeloBtn: Button
    private lateinit var dodajBiljkuBtn: Button
    private lateinit var scrollView : ScrollView
    private lateinit var uslikajBiljkuBtn: Button
    private lateinit var slikaIV: ImageView
    private lateinit var trefleDAO: TrefleDAO
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var database: BiljkaDatabase
    private lateinit var db : BiljkaDAO
    private val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trefleDAO = TrefleDAO()
        trefleDAO.setContext(this)
        database = BiljkaDatabase.buildDatabase(this@NovaBiljkaActivity)
        db = database.biljkaDao()
        setContentView(R.layout.nova_biljka_unos)
        initializeViews()
        setOnClickListeners()
        populateListViews()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("CameraDebug", "Picture taken")
        if (resultCode != RESULT_OK){
            return
        }
        when(requestCode){
            REQUEST_IMAGE_CAPTURE -> {
                val imageBitmap = data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    runOnUiThread {
                        Log.d("CameraDebug", "Image captured successfully")
                        Log.d("CameraDebug", "Image width: ${imageBitmap.width}, height: ${imageBitmap.height}")
                        Log.d("CameraDebug", "Image format: ${imageBitmap.config}")
                        slikaIV.setImageBitmap(imageBitmap)
                    }
                } else {
                    Log.w("CameraDebug", "No image data available in intent")
                    Toast.makeText(this, "Nema slike dostupne", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initializeViews() {
        nazivET = findViewById(R.id.nazivET)
        porodicaET = findViewById(R.id.porodicaET)
        medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
        jeloET = findViewById(R.id.jeloET)
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        profilOkusaLV = findViewById(R.id.profilOkusaLV)
        jelaLV = findViewById(R.id.jelaLV)
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
        slikaIV = findViewById(R.id.slikaIV)
        scrollView = findViewById(R.id.scrollView)
        medicinskaKoristLVET = findViewById(R.id.medicinskaKoristLVET)
        klimatskiTipLVET = findViewById(R.id.klimatskiTipLVET)
        zemljsniTipLVET = findViewById(R.id.zemljisniTipLVET)
        profilOkusaLVET = findViewById(R.id.profilOkusaLVET)
    }

    private fun setOnClickListeners() {
        dodajJeloBtn.setOnClickListener {
            val meal = jeloET.text.toString().trim()
            if (meal.isNotEmpty()) {
                var isValid = true
                val adapter = jelaLV.adapter
                for (i in 0 until adapter.count) {
                    val existingMeal = adapter.getItem(i)
                    if (existingMeal != null) {
                        val lowercaseExistingMeal = existingMeal.toString().lowercase(Locale.ROOT);
                        if (lowercaseExistingMeal.equals(meal, ignoreCase = true)) {
                            jeloET.error = "Ne možete dodati isto jelo više puta!"
                            isValid = false
                            break
                        }
                    }
                }

                // Nije nađen duplikat
                if (isValid) {
                    val adapter = jelaLV.adapter
                    if (adapter is ArrayAdapter<*>) {
                        val position = jelaLV.tag as Int?
                        if (position != null && position != -1) {
                            (adapter as ArrayAdapter<String>).remove(adapter.getItem(position)!!)
                            adapter.insert(meal, position)
                            jelaLV.tag = -1
                        } else {
                            (adapter as ArrayAdapter<String>).add(meal)
                        }
                        jeloET.setText("")
                        dodajJeloBtn.text = getString(R.string.dodaj_jelo)
                    } else {
                        Toast.makeText(this, "Greška u dodavanju jela", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                jeloET.error = "Unesite ime jela!"
            }
        }

        dodajBiljkuBtn.setOnClickListener {
            if (validateFields()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val trefleDAO = TrefleDAO()
                    trefleDAO.setContext(this@NovaBiljkaActivity)
                    addPlant(trefleDAO)
                }
            }
        }
        uslikajBiljkuBtn.setOnClickListener {

            uslikajBiljkuBtn.isEnabled = false
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Nema dostupne kamere na uređaju", Toast.LENGTH_SHORT).show()
            }
            uslikajBiljkuBtn.isEnabled = true
        }

        jelaLV.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = jelaLV.getItemAtPosition(position).toString()
            jeloET.setText(selectedItem)
            jelaLV.tag = position
            dodajJeloBtn.text = getString(R.string.izmijeni_jelo)
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        // nazivET
        val naziv = nazivET.text.toString().trim()
        if (naziv.length !in 2..40){
            nazivET.error = "Naziv biljke mora imati između 2 i 40 znakova!"
            isValid = false
        }
        else{
            nazivET.error = null
        }
        // porodicaET
        val porodica = porodicaET.text.toString().trim()
        if (porodica.length !in 2..20) {
            porodicaET.error = "Porodica biljke mora imati između 2 i 20 znakova!"
            isValid = false
        } else {
            porodicaET.error = null
        }

        //medicinskoUpozorenjeET
        val upozorenje = medicinskoUpozorenjeET.text.toString().trim()
        if (upozorenje.length !in 2..20) {
            medicinskoUpozorenjeET.error = "Medicinsko upozorenje mora imati između 2 i 20 znakova!"
            isValid = false
        } else {
            medicinskoUpozorenjeET.error = null
        }

        // jelaLV
        val adapter = jelaLV.adapter as ArrayAdapter<String>
        if (adapter.isEmpty) {
            jeloET.error = "Morate dodati barem jedno jelo!"
            isValid = false
        }

        // medicinskaKoristLV
        val selectedMedicinskaKorist = medicinskaKoristLV.checkedItemCount
        if (selectedMedicinskaKorist < 1) {
            medicinskaKoristLVET.error = "Morate odabrati barem jednu medicinsku korist!"
            isValid = false
        }

        // klimatskiTipLV
        val selectedKlimatskiTip = klimatskiTipLV.checkedItemCount
        if (selectedKlimatskiTip < 1) {
            klimatskiTipLVET.error = "Morate odabrati barem jedan klimatski tip!"
            isValid = false
        }

        // zemljisniTipLV
        val selectedZemljisniTip = zemljisniTipLV.checkedItemCount
        if (selectedZemljisniTip < 1) {
            zemljsniTipLVET.error = "Morate odabrati barem jedan zemljisni tip!"
            isValid = false
        }

        // profilOkusaLV
        val selectedProfilOkusa = profilOkusaLV.checkedItemCount
        if (selectedProfilOkusa < 1 || selectedProfilOkusa > 1) {
            profilOkusaLVET.error = "Morate odabrati jedan profil okusa!"
            isValid = false
        }
        return isValid
    }

    private fun populateListViews() {
        val medicinskaKoristAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, MedicinskaKorist.entries.map { it.opis })
        medicinskaKoristLV.adapter = medicinskaKoristAdapter

        val klimatskiTipAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, KlimatskiTip.entries.map { it.opis })
        klimatskiTipLV.adapter = klimatskiTipAdapter

        val zemljisniTipAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, Zemljiste.entries.map { it.naziv })
        zemljisniTipLV.adapter = zemljisniTipAdapter

        val profilOkusaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, ProfilOkusaBiljke.entries.map { it.opis })
        profilOkusaLV.adapter = profilOkusaAdapter

        val jelaAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        jelaLV.adapter = jelaAdapter
    }

    private suspend fun addPlant(trefleDAO: TrefleDAO) {
        val naziv = nazivET.text.toString().trim()
        val porodica = porodicaET.text.toString().trim()
        val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString().trim()

        val medicinskeKoristi = mutableListOf<MedicinskaKorist>()
        val checkedItemsMedicinskeKoristi = medicinskaKoristLV.checkedItemPositions
        for (i in 0 until checkedItemsMedicinskeKoristi.size()) {
            if (checkedItemsMedicinskeKoristi.valueAt(i)) {
                medicinskeKoristi.add(MedicinskaKorist.entries[checkedItemsMedicinskeKoristi.keyAt(i)])
            }
        }

        val profilOkusa = ProfilOkusaBiljke.entries[profilOkusaLV.checkedItemPosition]

        val jela = mutableListOf<String>()
        val jelaAdapter = jelaLV.adapter as ArrayAdapter<*>
        for (i in 0 until jelaAdapter.count) {
            jela.add(jelaAdapter.getItem(i).toString())
        }

        val klimatskiTipovi = mutableListOf<KlimatskiTip>()
        val checkedItemsKlimatskiTipovi = klimatskiTipLV.checkedItemPositions
        for (i in 0 until checkedItemsKlimatskiTipovi.size()) {
            if (checkedItemsKlimatskiTipovi.valueAt(i)) {
                klimatskiTipovi.add(KlimatskiTip.entries[checkedItemsKlimatskiTipovi.keyAt(i)])
            }
        }

        val zemljisniTipovi = mutableListOf<Zemljiste>()
        val checkedItemsZemljisniTipovi = zemljisniTipLV.checkedItemPositions
        for (i in 0 until checkedItemsZemljisniTipovi.size()) {
            if (checkedItemsZemljisniTipovi.valueAt(i)) {
                zemljisniTipovi.add(Zemljiste.entries[checkedItemsZemljisniTipovi.keyAt(i)])
            }
        }

        val newPlant = Biljka(
            0,
            naziv,
            porodica,
            medicinskoUpozorenje,
            medicinskeKoristi,
            profilOkusa,
            jela,
            klimatskiTipovi,
            zemljisniTipovi,
            false
        )
        val fixedPlant = trefleDAO.fixData(newPlant)
        if (fixedPlant != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val success = db.saveBiljka(newPlant)
                Log.d("NovaBiljkaActivity", "Success adding plant :  $success")
            }
            MainActivity.biljke.add(fixedPlant)
            Log.d("NovaBiljkaActivity", "Biljka added after fixData")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Log.w("NovaBiljkaActivity", "fixData returned null - Biljka not added")
            Toast.makeText(this, "Greška prilikom dodavanja biljke", Toast.LENGTH_SHORT).show()
        }

        Log.d("NovaBiljkaActivity", "Size of Biljke list before adding new plant: ${MainActivity.biljke.size}")
        Log.d("NovaBiljkaActivity", "Size of Biljke list after adding new plant: ${MainActivity.biljke.size}")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}




