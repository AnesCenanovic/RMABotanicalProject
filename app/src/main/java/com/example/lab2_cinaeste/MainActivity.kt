
package com.example.lab2_cinaeste

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    enum class TipoviBiljaka {
        MEDICINSKA,
        BOTANICKA,
        KUHARSKA
    }
    companion object {
        val biljke = mutableListOf(
            Biljka(
                naziv = "Bosiljak Ocimum basilicum",
                porodica = "Lamiaceae (usnate)",
                medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
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
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Pečeno pile", "Grah","Gulaš"),
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
            ),
            Biljka(
                naziv = "Peršun (Petroselinum crispum)",
                porodica = "Apiaceae (štitarka)",
                medicinskoUpozorenje = "Osobe koje pate od alergija na korijandar, mrkvu ili mašću također mogu biti alergične na peršin.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPROBAVE),
                profilOkusa = ProfilOkusaBiljke.LJUTO,
                jela = listOf("Juha od peršuna", "Salata od peršina"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ),
            Biljka(
                naziv = "Kopriva (Urtica dioica)",
                porodica = "Urticaceae (koprivke)",
                medicinskoUpozorenje = "Izbjegavajte dodir s koprivom ako ste alergični na nju.",
                medicinskeKoristi = listOf(MedicinskaKorist.IMMUNOSUPORT, MedicinskaKorist.SMIRENJE),
                profilOkusa = ProfilOkusaBiljke.LJUTO,
                jela = listOf("Čaj od koprive", "Pesto od koprive"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
                zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.KRECNJACKO)
            ),
            Biljka(
                naziv = "Matičnjak (Melissa officinalis)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Oprez kod osoba s hipotireozom, budući da može sniziti razinu hormona.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.IMMUNOSUPORT),
                profilOkusa = ProfilOkusaBiljke.CITRUSNI,
                jela = listOf("Čaj od matičnjaka", "Koktel s matičnjakom"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
                zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.PJESKOVITO)
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
                naziv = "Hibiscus (Hibiscus sabdariffa)",
                porodica = "Malvaceae (sljezovke)",
                medicinskoUpozorenje = "Ne preporučuje se za trudnice i dojilje.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
                profilOkusa = ProfilOkusaBiljke.CITRUSNI,
                jela = listOf("Čaj od hibiskusa", "Hibiskusni sok"),
                klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.SLJUNOVITO)
            )
        )
    }


    private lateinit var spinner: Spinner
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var biljkeRV : List<Biljka>

    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var kuharskiAdapter: KuharskiAdapter

    private var filtered: List<Biljka> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        plantRecyclerView = findViewById(R.id.biljkeRV)
        plantRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        biljkeRV = getAllBiljke();

        medicinskiAdapter = MedicinskiAdapter(biljkeRV, { clickedPlant ->
            val filteredPlants = biljkeRV.filter { plant ->
                plant.medicinskeKoristi.any { korist ->
                    clickedPlant.medicinskeKoristi.contains(korist)
                }
            }
            filtered= filteredPlants;
            medicinskiAdapter.updatePlants(filteredPlants)

        },this)
        botanickiAdapter = BotanickiAdapter(biljkeRV, { clickedPlant ->

                val filteredPlants = biljkeRV.filter { plant ->
                    plant.porodica == clickedPlant.porodica &&
                            plant.zemljisniTipovi.any { soil -> clickedPlant.zemljisniTipovi.contains(soil) }
                }
                filtered = filteredPlants;
                botanickiAdapter.updatePlants(filteredPlants)
            },this)

        kuharskiAdapter = KuharskiAdapter(biljkeRV, { clickedPlant ->
            val filteredPlants = biljkeRV.filter { plant ->
                plant.profilOkusa == clickedPlant.profilOkusa ||
                        plant.jela.intersect(clickedPlant.jela).isNotEmpty()
            }
            filtered = filteredPlants;
            kuharskiAdapter.updatePlants(filteredPlants)
        },this)

        spinner = findViewById(R.id.modSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedAdapter = when (position) {
                    0 -> medicinskiAdapter.apply { if (filtered.isNotEmpty()) {
                        updatePlants(filtered)
                    } else {
                        updatePlants(biljkeRV)
                    }}
                    1 -> botanickiAdapter.apply { if (filtered.isNotEmpty()) {
                        updatePlants(filtered)
                    } else {
                        updatePlants(biljkeRV)
                    } }
                    2 -> kuharskiAdapter.apply { if (filtered.isNotEmpty()) {
                        updatePlants(filtered)
                    } else {
                        updatePlants(biljkeRV)
                    } }
                    else -> medicinskiAdapter.apply { if (filtered.isNotEmpty()) {
                        updatePlants(filtered)
                    } else {
                        updatePlants(biljkeRV)
                    }}
                }
                plantRecyclerView.adapter = selectedAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        spinner.setSelection(0) // default
        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            medicinskiAdapter.resetPlants(biljkeRV)
            botanickiAdapter.resetPlants(biljkeRV)
            kuharskiAdapter.resetPlants(biljkeRV)
        }
        val novaBiljkaBtn = findViewById<Button>(R.id.novaBiljkaBtn)
        novaBiljkaBtn.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            startActivity(intent)
        }
    }

    fun getAllBiljke(): List<Biljka> {
        return biljke;
    }

    class MedicinskiAdapter(
        private var biljkeRV: List<Biljka>,
        private val onItemClick: (Biljka) -> Unit,
        private val context: Context
    ) : RecyclerView.Adapter<MedicinskiAdapter.MedicinskiViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinskiViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_layout, parent, false)
            return MedicinskiViewHolder(view)
        }

        override fun getItemCount(): Int = biljkeRV.size

        override fun onBindViewHolder(holder: MedicinskiViewHolder, position: Int) {
            Log.d("MedicinskiAdapter", "onBindViewHolder called for position: $position")
            val plant = biljkeRV[position]
            holder.itemView.setOnClickListener { onItemClick(plant) }

            holder.itemView.setOnClickListener {
                onItemClick(plant)
            }

            Log.d("MedicinskiAdapter", "Binding MedicinskiViewHolder")
            holder.nazivBiljke.text = plant.naziv
            holder.tekstUpozorenja.text = plant.medicinskoUpozorenje
            if (plant.medicinskeKoristi.isNotEmpty()) {
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

            val trefleDAO = TrefleDAO(context)
            CoroutineScope(Dispatchers.IO).launch{
                val image = trefleDAO.getImage(plant)
                withContext(Dispatchers.Main) {
                    holder.plantImage.setImageBitmap(image)
                }
            }
        }
        fun updatePlants(plants: List<Biljka>) {
            this.biljkeRV = plants
            notifyDataSetChanged()
        }
        inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
            val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
            val tekstUpozorenja: TextView = itemView.findViewById(R.id.upozorenjeItem)
            val korist1: TextView = itemView.findViewById(R.id.korist1Item)
            val korist2: TextView = itemView.findViewById(R.id.korist2Item)
            val korist3: TextView = itemView.findViewById(R.id.korist3Item)
        }
        fun resetPlants(newPlants: List<Biljka>) {
            biljkeRV = newPlants
            notifyDataSetChanged()
        }
    }

    class BotanickiAdapter(
        private var biljkeRV: List<Biljka>,
        private val onItemClick: (Biljka) -> Unit,
        private val context: Context
    ) : RecyclerView.Adapter<BotanickiAdapter.BotanickiViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki_layout, parent, false)
            return BotanickiViewHolder(view)
        }

        override fun getItemCount(): Int = biljkeRV.size

        override fun onBindViewHolder(holder: BotanickiViewHolder, position: Int) {
            Log.d("PlantListAdapter", "Binding BotanickiViewHolder")
            val plant = biljkeRV[position]

            holder.itemView.setOnClickListener {
                onItemClick(plant)
            }

            holder.nazivBiljke.text = plant.naziv
            holder.porodica.text = plant.porodica
            if (plant.klimatskiTipovi.isNotEmpty()) {
                holder.prviKlimatskiTip.text = plant.klimatskiTipovi[0].opis
            } else {
                holder.prviKlimatskiTip.text = ""
            }
            if (plant.zemljisniTipovi.isNotEmpty()) {
                holder.prviTipZemljista.text = plant.zemljisniTipovi[0].naziv
            } else {
                holder.prviTipZemljista.text = ""
            }

            val trefleDAO = TrefleDAO(context)
            CoroutineScope(Dispatchers.IO).launch{
                val image = trefleDAO.getImage(plant)
                withContext(Dispatchers.Main){
                    holder.plantImage.setImageBitmap(image)
                }
            }
        }
        fun updatePlants(plants: List<Biljka>) {
            this.biljkeRV = plants
            notifyDataSetChanged()
        }
        inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
            val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
            val porodica: TextView = itemView.findViewById(R.id.porodicaItem)
            val prviKlimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
            val prviTipZemljista: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        }
        fun resetPlants(newPlants: List<Biljka>) {
            biljkeRV = newPlants
            notifyDataSetChanged()
        }
    }

    class KuharskiAdapter(
        private var biljkeRV: List<Biljka>,
        private val onItemClick: (Biljka) -> Unit,
        private val context: Context
    ) : RecyclerView.Adapter<KuharskiAdapter.KuharskiViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_layout, parent, false)
            return KuharskiViewHolder(view)
        }
        override fun getItemCount(): Int = biljkeRV.size

        override fun onBindViewHolder(holder: KuharskiViewHolder, position: Int) {
            Log.d("PlantListAdapter", "Binding KuharskiViewHolder")
            val plant = biljkeRV[position]

            holder.itemView.setOnClickListener {
                onItemClick(plant)
            }

            holder.nazivBiljke.text = plant.naziv
            holder.profilOkusa.text = buildString {
                append("biljni okus: ")
                append(plant.profilOkusa.toString())
            }
            if (plant.jela.size >= 1) {
                holder.jelo1.text = plant.jela[0]
            } else {
                holder.jelo1.text = ""
            }
            if (plant.jela.size >= 2) {
                holder.jelo2.text = plant.jela[1]
            } else {
                holder.jelo2.text = ""
            }
            if (plant.jela.size >= 3) {
                holder.jelo3.text = plant.jela[2]
            } else {
                holder.jelo3.text = ""
            }

            val trefleDAO = TrefleDAO(context)
            CoroutineScope(Dispatchers.IO).launch{
                val image = trefleDAO.getImage(plant)
                withContext(Dispatchers.Main){
                    holder.plantImage.setImageBitmap(image)
                }
            }
        }
        fun updatePlants(plants: List<Biljka>) {
            this.biljkeRV = plants
            notifyDataSetChanged()
        }
        inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
            val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
            val profilOkusa: TextView = itemView.findViewById(R.id.profilOkusaItem)
            val jelo1: TextView = itemView.findViewById(R.id.jelo1Item)
            val jelo2: TextView = itemView.findViewById(R.id.jelo2Item)
            val jelo3: TextView = itemView.findViewById(R.id.jelo3Item)
        }
        fun resetPlants(newPlants: List<Biljka>) {
            biljkeRV = newPlants
            notifyDataSetChanged()
        }
    }
}