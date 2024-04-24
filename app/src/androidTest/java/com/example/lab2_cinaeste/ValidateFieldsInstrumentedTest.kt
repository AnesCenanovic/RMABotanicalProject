package com.example.lab2_cinaeste


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ValidateFieldsInstrumentedTest {

    @get:Rule
    @JvmField
    val activityRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    @Test
    fun validateNazivETInvalid() {
        onView(withId(R.id.nazivET)).perform(replaceText("A"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv biljke mora imati između 2 i 20 znakova!")))
    }
    @Test
    fun validateNazivETValid() {
        onView(withId(R.id.nazivET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(not(hasErrorText("Naziv biljke mora imati između 2 i 20 znakova!"))))
    }
    @Test
    fun validatePorodicaETInvalid() {
        onView(withId(R.id.porodicaET)).perform(replaceText("A"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica biljke mora imati između 2 i 20 znakova!")))
    }
    @Test
    fun validatePorodicaETValid() {
        onView(withId(R.id.porodicaET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(not(hasErrorText("Porodica biljke mora imati između 2 i 20 znakova!"))))
    }
    @Test
    fun validateMedicinskoUpozorenjeETInvalid() {
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(replaceText("A"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Medicinsko upozorenje mora imati između 2 i 20 znakova!")))
    }
    @Test
    fun validateMedicinskoUpozorenjeETValid() {
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(not(hasErrorText("Medicinsko upozorenje mora imati između 2 i 20 znakova!"))))
    }
    @Test
    fun validatejeloETValid() {
        onView(withId(R.id.jeloET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(not(hasErrorText("Morate dodati barem jedno jelo!"))))
    }
    @Test
    fun validatejeloETEmpty() {
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches((hasErrorText("Unesite ime jela!"))))
    }
    @Test
    fun validatejeloETDuplikat() {
        onView(withId(R.id.jeloET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.scrollView)).perform(swipeDown())

        onView(withId(R.id.jeloET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.jeloET)).check(matches((hasErrorText("Ne možete dodati isto jelo više puta!"))))
    }
    @Test
    fun validatejelaLVEmpty() {
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches((hasErrorText("Morate dodati barem jedno jelo!"))))
    }
}