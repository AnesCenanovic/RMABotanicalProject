package com.example.lab2_cinaeste



import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class ValidateFieldsInstrumentedTest {

    @get:Rule
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
        onView(withId(R.id.dodajJeloBtn)).perform(click())
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
    fun validatejeloETButton() {
        onView(withId(R.id.jeloET)).perform(replaceText("Aloe"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        val currentView = activityRule.scenario.onActivity {
            val listView = it.findViewById<ListView>(R.id.jelaLV)
            listView.performItemClick(listView, 0, 0)
        }
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).check(matches(withText("Izmijeni jelo")));
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

    @Test
    // Isto važi za ostale multiple choice listView
    fun validateMedicinskaKoristLVInvalid() {
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskaKoristLVET)).check(matches((hasErrorText("Morate odabrati barem jednu medicinsku korist!"))))
    }

    @Test
    fun validateMedicinskaKoristLVValid() {
        val currentView = activityRule.scenario.onActivity {
            val listView = it.findViewById<ListView>(R.id.medicinskaKoristLV)
            listView.performItemClick(listView, 1, 0)
            listView.performItemClick(listView, 2, 0)
        }
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskaKoristLVET)).check((matches(not(hasErrorText("Morate odabrati barem jednu medicinsku korist!")))))
    }

    // Single choice listView
    @Test
    fun validateProfilOkusaLVInvalid() {
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.profilOkusaLVET)).check(matches((hasErrorText("Morate odabrati jedan profil okusa!"))))
    }

    @Test
    fun validateProfilOkusaLVMultipleChoiceInvalid() {
        val currentView = activityRule.scenario.onActivity {
            val listView = it.findViewById<ListView>(R.id.profilOkusaLV)
            listView.performItemClick(listView, 1, 0)
        }
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.profilOkusaLVET)).check((matches(not(hasErrorText("Morate odabrati jedan profil okusa!")))))
    }

    @Test
    fun validateUslikajBiljku() {
        val mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        activityRule.scenario.onActivity { activity ->
            val resultIntent = Intent().apply {
                putExtras(Bundle().apply { putParcelable("data", mockBitmap) })
                val REQUEST_IMAGE_CAPTURE = 100
                activity.onActivityResult(
                    REQUEST_IMAGE_CAPTURE,
                    Activity.RESULT_OK,
                    this
                ) // Simulate onActivityResult
            }
        }
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.slikaIV)).check(matches(isDisplayed()))
        onView(withId(R.id.slikaIV)).check(matches(withSameBitmap(mockBitmap)))

    }

    private fun withSameBitmap(expectedBitmap: Bitmap) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Drawable does not match with expected bitmap")
        }

        override fun matchesSafely(item: View): Boolean {
            if (item !is ImageView) {
                return false
            }
            val actualBitmap = (item as ImageView).drawable.toBitmap()
            return expectedBitmap.sameAs(actualBitmap)
        }
    }
}

