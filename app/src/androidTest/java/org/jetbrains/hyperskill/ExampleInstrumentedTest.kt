package org.jetbrains.hyperskill

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.jetbrains.hyperskill.model.HyperSkillUser
import org.jetbrains.hyperskill.model.HyperSkillUserStats
import org.jetbrains.hyperskill.network.ApiService

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("org.jetbrains.hyperskill", appContext.packageName)
    }

    @Test
    fun testProfileLoader() {
        val expected = HyperSkillUser(
            id = 1283850,
            name = "Vladimir Klimov",
            stats = HyperSkillUserStats(6, 0, 6),
            bio = "Product Data Analyst, Bioinformatist, Zoologist, Molecular Biologist",
            avatarUrl = "https://ucarecdn.com/efff3079-1b03-4f5c-bbf2-dd2a8d9d49e5/-/crop/1706x1707/436,0/-/preview/",
            experience = "https://www.sciencedirect.com/science/article/abs/pii/S1434461016300426",
            country = "RU",
            languages = listOf("en", "ru")
        )
        val actual = ApiService.getUserData("1283850")
        assertEquals(expected, actual)
    }
}