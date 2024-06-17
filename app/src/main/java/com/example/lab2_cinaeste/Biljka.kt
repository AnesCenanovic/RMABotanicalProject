package com.example.lab2_cinaeste


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "Biljka")
@TypeConverters(MedicinskaKoristConverter::class, ProfilOkusaConverter::class, JelaConverter::class, KlimatskiTipConverter::class, ZemljisteConverter::class)
data class Biljka(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val naziv: String,
    var porodica: String,
    var medicinskoUpozorenje: String,
    @TypeConverters(MedicinskaKoristConverter::class) var medicinskeKoristi: List<MedicinskaKorist>,
    @TypeConverters(ProfilOkusaConverter::class) val profilOkusa: ProfilOkusaBiljke?,
    @TypeConverters(JelaConverter::class) var jela: List<String> = emptyList(),
    @TypeConverters(KlimatskiTipConverter::class) var klimatskiTipovi: List<KlimatskiTip>,
    @TypeConverters(ZemljisteConverter::class) var zemljisniTipovi: List<Zemljiste>?,
    var onlineChecked: Boolean
)

class MedicinskaKoristConverter {
    @TypeConverter
    fun fromList(medicinskeKoristi: List<MedicinskaKorist>): String {
        return medicinskeKoristi.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toList(data: String): List<MedicinskaKorist> {
        return data.split(",").map { MedicinskaKorist.valueOf(it) }
    }
}

class ProfilOkusaConverter {
    @TypeConverter
    fun fromEnum(profilOkusa: ProfilOkusaBiljke): String {
        return profilOkusa.name
    }

    @TypeConverter
    fun toEnum(data: String): ProfilOkusaBiljke {
        return ProfilOkusaBiljke.valueOf(data)
    }
}

class JelaConverter {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return data.split(",")
    }
}

class KlimatskiTipConverter {
    @TypeConverter
    fun fromList(klimatskiTipovi: List<KlimatskiTip>): String {
        return klimatskiTipovi.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toList(data: String): List<KlimatskiTip> {
        return data.split(",").map { KlimatskiTip.valueOf(it) }
    }
}

class ZemljisteConverter {
    @TypeConverter
    fun fromList(zemljisniTipovi: List<Zemljiste>): String {
        return zemljisniTipovi.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toList(data: String): List<Zemljiste> {
        if (data.isEmpty()) {
            return emptyList()
        }
        return data.split(",").map { Zemljiste.valueOf(it) }
    }
}