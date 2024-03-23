package com.example.lab2_cinaeste

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantListAdapter: PlantListAdapter
    private lateinit var biljkeRV : List<Biljka>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        plantRecyclerView = findViewById(R.id.biljkeRV)
        plantRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        biljkeRV = getAllBiljke();
        plantListAdapter = PlantListAdapter(listOf())
        plantRecyclerView.adapter = plantListAdapter
        plantListAdapter.updatePlants(biljkeRV)
        spinner = findViewById(R.id.modSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    data class Biljka(
        val naziv: String,
        val porodica: String,
        val medicinskoUpozorenje: String,
        val medicinskeKoristi: List<MedicinskaKorist>,
        val profilOkusa: ProfilOkusaBiljke,
        val jela: List<String>,
        val klimatskiTipovi: List<KlimatskiTip>,
        val zemljisniTipovi: List<Zemljiste>
    )

    enum class TipoviBiljaka {
        MEDICINSKA,
        BOTANICKA,
        KUHARSKA
    }

    enum class Zemljiste(val naziv: String) {
        PJESKOVITO("Pjeskovito zemljište"),
        GLINENO("Glinеno zemljište"),
        ILOVACA("Ilovača"),
        CRNICA("Crnica"),
        SLJUNOVITO("Šljunovito zemljište"),
        KRECNJACKO("Krečnjačko zemljište");
    }

    enum class KlimatskiTip(val opis: String) {
        SREDOZEMNA("Mediteranska klima - suha, topla ljeta i blage zime"),
        TROPSKA("Tropska klima - topla i vlažna tokom cijele godine"),
        SUBTROPSKA("Subtropska klima - blage zime i topla do vruća ljeta"),
        UMJERENA("Umjerena klima - topla ljeta i hladne zime"),
        SUHA("Sušna klima - niske padavine i visoke temperature tokom cijele godine"),
        PLANINSKA("Planinska klima - hladne temperature i kratke sezone rasta"),
    }

    enum class MedicinskaKorist(val opis: String) {
        SMIRENJE("Smirenje - za smirenje i relaksaciju"),
        PROTUUPALNO("Protuupalno - za smanjenje upale"),
        PROTIVBOLOVA("Protivbolova - za smanjenje bolova"),
        REGULACIJAPRITISKA("Regulacija pritiska - za regulaciju visokog/niskog pritiska"),
        REGULACIJAPROBAVE("Regulacija probave"),
        IMMUNOSUPORT("Podrška imunitetu"),
    }

    enum class ProfilOkusaBiljke(val opis: String) {
        MENTA("Mentol - osvježavajući, hladan ukus"),
        CITRUSNI("Citrusni - osvježavajući, aromatičan"),
        SLATKI("Sladak okus"),
        BEZUKUSNO("Obični biljni okus - travnat, zemljast ukus"),
        LJUTO("Ljuto ili papreno"),
        KORIJENASTO("Korenast - drvenast i gorak ukus"),
        AROMATICNO("Začinski - topli i aromatičan ukus"),
        GORKO("Gorak okus"),
    }

    fun getAllBiljke(): List<Biljka> {
        return biljke;
    }

    val biljke = listOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            jela = listOf("Jogurt sa voćem", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Ružmarin (Rosmarinus officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO,
                MedicinskaKorist.REGULACIJAPRITISKA
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Grah", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Lavanda (Lavandula angustifolia)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.IMMUNOSUPORT),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Jogurt sa voćem"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        )
    )

    class PlantListAdapter(
        private var biljkeRV: List<Biljka>
    ) : RecyclerView.Adapter<PlantListAdapter.PlantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.medicinski_layout, parent, false)
            return PlantViewHolder(view)
        }

        override fun getItemCount(): Int = biljkeRV.size
        override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {

            val plant = biljkeRV[position]
            holder.nazivBiljke.text = plant.naziv
            holder.upozorenje.text = plant.medicinskoUpozorenje
            if (plant.medicinskeKoristi.size >= 1) {
                holder.korist1.text = plant.medicinskeKoristi[0].opis
            } else {
                holder.korist1.text = ""
            }
            if (plant.medicinskeKoristi.size >= 2) {
                holder.korist2.text = plant.medicinskeKoristi[1].opis
            } else {
                holder.korist2.text = ""
            }
            if (plant.medicinskeKoristi.size >= 3) {
                holder.korist3.text = plant.medicinskeKoristi[2].opis
            } else {
                holder.korist3.text = ""
            }
        }

        fun updatePlants(plants: List<Biljka>) {
            this.biljkeRV = plants
            notifyDataSetChanged()
        }

        inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
            val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
            val upozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
            val korist1: TextView = itemView.findViewById(R.id.korist1Item)
            val korist2: TextView = itemView.findViewById(R.id.korist2Item)
            val korist3: TextView = itemView.findViewById(R.id.korist3Item)
        }
    }
}
