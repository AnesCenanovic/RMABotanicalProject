package com.example.lab2_cinaeste

data class Biljka(
    val naziv: String,
    var porodica: String,
    var medicinskoUpozorenje: String,
    var medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke?,
    var jela: List<String> = emptyList(),
    var klimatskiTipovi: List<KlimatskiTip>,
    var zemljisniTipovi: List<Zemljiste>
)