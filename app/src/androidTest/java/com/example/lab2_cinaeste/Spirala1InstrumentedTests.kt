package com.example.lab2_cinaeste





import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class Spirala1InstrumentedTests {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun prikazujuSePocetneBiljke() {
        val recyclerView = activityScenarioRule.scenario.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.biljkeRV)
            val itemCount = recyclerView.adapter!!.itemCount
            assertThat(itemCount, greaterThan(4))
        }
        val listaNaziva = listOf(
            "Bosiljak (Ocimum basilicum)",
            "Nana (Mentha spicata)",
            "Kamilica (Matricaria chamomilla)",
            "Ružmarin (Rosmarinus officinalis)",
            "Lavanda (Lavandula angustifolia)"
        )
        for (naziv in listaNaziva)
            onView(withId(R.id.biljkeRV)).perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText(naziv)),
                        hasDescendant(withId(R.id.nazivItem))
                    )
                )
            )

    }

    @Test
    fun spinnerImaSveModove() {
        val listaNazivaModova = listOf("Medicin", "Kuha", "Botan")
        for (naziv in listaNazivaModova) {
            onView(withId(R.id.modSpinner)).perform(click())
            onData(
                allOf(
                    Is(instanceOf(String::class.java)),
                    containsString(naziv)
                )
            ).perform(click())
        }
    }

    @Test
    fun modChange() {
        onView(withId(R.id.modSpinner)).perform(click())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Medicin")
            )
        ).perform(click())
        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                    hasDescendant(withText(containsString("Smirenje")))
                )
            )
        )


        onView(withId(R.id.modSpinner)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), containsString("Kuha"))).perform(click())
        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                    not(hasDescendant(withText(containsString("Smirenje")))),
                    hasDescendant(withText(containsString("Salata")))
                )
            )
        )
    }

    @Test
    fun onFilterInMedicinskiModBosiljak() {
        onView(withId(R.id.modSpinner)).perform(click())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Medicin")
            )
        ).perform(click())
        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                    hasDescendant(withText(containsString("Smirenje")))
                ), click()
            )
        )
        val listaVidljivihNaziva = listOf(
            "Bosiljak (Ocimum basilicum)",
            "Kamilica (Matricaria chamomilla)",
            "Lavanda (Lavandula angustifolia)"
        )
        val listaNevidljivihNaziva =
            listOf("Nana (Mentha spicata)", "RuÅ¾marin (Rosmarinus officinalis)")
        for (naziv in listaVidljivihNaziva)
            onView(withId(R.id.biljkeRV)).perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText(naziv)),
                        hasDescendant(withId(R.id.nazivItem))
                    )
                )
            )
        for (naziv in listaNevidljivihNaziva) {
            try {
                onView(withId(R.id.biljkeRV)).perform(
                    RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                        allOf(
                            hasDescendant(withText(naziv)),
                            hasDescendant(withId(R.id.nazivItem))
                        )
                    )
                )
                assert(
                    false,
                    { "Scroll treba pasti sljedeÄ‡a biljka se prikazuje nakon filtriranja a ne treba. Biljka:  " + naziv })
            } catch (e: Exception) {
                assertThat(e.message, containsString("Error performing"))
            }
        }
    }

    @Test
    fun svakiModImaIspravneIdeve() {
        onView(withId(R.id.modSpinner)).perform(click())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Medicin")
            )
        ).perform(click())
        val listMedicinskiIds = listOf(
            R.id.nazivItem,
            R.id.slikaItem,
            R.id.korist1Item,
            R.id.korist2Item,
            R.id.korist3Item,
            R.id.upozorenjeItem
        )
        for (id in listMedicinskiIds) {
            onView(withId(R.id.biljkeRV)).perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                        hasDescendant(withText(containsString("Smirenje"))),
                        hasDescendant(withId(id))
                    )
                )
            )
        }

        onView(withId(R.id.modSpinner)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), containsString("Kuh"))).perform(click())
        val listKuharskiIds = listOf(
            R.id.nazivItem,
            R.id.slikaItem,
            R.id.jelo1Item,
            R.id.jelo2Item,
            R.id.jelo3Item,
            R.id.profilOkusaItem
        )
        for (id in listKuharskiIds) {
            onView(withId(R.id.biljkeRV)).perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                        hasDescendant(withText(containsString("biljni okus"))),
                        hasDescendant(withId(id))
                    )
                )
            )
        }

        onView(withId(R.id.modSpinner)).perform(click())
        onData(allOf(Is(instanceOf(String::class.java)), containsString("Botan"))).perform(click())
        val listBotanickiIds = listOf(
            R.id.nazivItem,
            R.id.slikaItem,
            R.id.porodicaItem,
            R.id.zemljisniTipItem,
            R.id.klimatskiTipItem
        )
        for (id in listBotanickiIds) {
            onView(withId(R.id.biljkeRV)).perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText("Bosiljak (Ocimum basilicum)")),
                        hasDescendant(withText(containsString("Mediteranska"))),
                        hasDescendant(withId(id))
                    )
                )
            )
        }

    }


}