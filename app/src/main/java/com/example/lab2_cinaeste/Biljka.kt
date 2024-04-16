package com.example.lab2_cinaeste

data class Biljka(
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MainActivity.MedicinskaKorist>,
    val profilOkusa: MainActivity.ProfilOkusaBiljke,
    val jela: List<String>,
    val klimatskiTipovi: List<MainActivity.KlimatskiTip>,
    val zemljisniTipovi: List<MainActivity.Zemljiste>
)