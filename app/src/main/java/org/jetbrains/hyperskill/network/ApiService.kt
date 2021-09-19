package org.jetbrains.hyperskill.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.hyperskill.model.HyperSkillUser
import org.jetbrains.hyperskill.model.HyperSkillUserStats
import org.json.JSONArray
import org.json.JSONObject


object ApiService {
    private const val hyperSkillUrl = "https://hyperskill.org/"
    private const val hyperSkillApiUrl = "${hyperSkillUrl}api/"
    private const val hyperSkillLoginUrl = hyperSkillUrl + "login?next=%2Ftracks"

    private fun JSONArray.toList(): List<String> {
        return (0 until this.length()).map { this.getString(it) }
    }

    fun getUserData(id: String): HyperSkillUser {
        return runBlocking {
            val response = HttpClient().use { httpClient ->
                httpClient.get<String>(hyperSkillApiUrl + "users/" + id)
            }
            // TODO handle errors
            val json = JSONObject(response).getJSONArray("users").getJSONObject(0)
            HyperSkillUser(
                id = json.getLong("id"),
                name = json.getString("fullname"),
                stats = HyperSkillUserStats(
                    projectsCompleted = json.getJSONObject("gamification").getInt("passed_projects"),
                    tracksCompleted = json.getJSONArray("completed_tracks").length(),
                    badgesReceived = getBadgesCount(id),
                ),
                avatarUrl = json.getString("avatar"),
                bio = json.getString("bio").takeIf { it.isNotEmpty() },
                experience = json.getString("experience").takeIf { it.isNotEmpty() },
                country = json.getString("country").takeIf { it.isNotEmpty() && it != "null" },
                languages = json.getJSONArray("languages").toList().takeIf { it.isNotEmpty() },
            )
        }
    }

    private suspend fun getCookies(): Map<String, String> {
        val response = HttpClient().use { httpClient ->
            httpClient.get<HttpResponse>(hyperSkillLoginUrl).headers["set-cookie"]
        }

        // TODO add cookie with all properties
        return response!!.split(";").map {
            val cookie = it.split("=")
            cookie[0] to cookie[1]
        }.toMap()
    }

    private suspend fun getBadgesCount(id: String): Int {
        val response = HttpClient().use { httpClient ->
            httpClient.get<String>(hyperSkillApiUrl + "badges?user=$id")
        }

        val badges = JSONObject(response).getJSONArray("badges")
        return (0 until badges.length()).count {
            badges.getJSONObject(it).getInt("level") != 0
        }
    }

    fun login(email: String, password: String): ResponseResult {
        return runBlocking {
            val cookies = getCookies().filter { it.key == "csrftoken" }

            HttpClient().use { httpClient ->
                try {
                    val response = httpClient.post<String>(hyperSkillApiUrl + "profiles/login") {
                        contentType(ContentType.Application.Json)
                        body = "{\"email\":\"$email\",\"password\":\"$password\"}"
                        cookies.forEach { cookie(it.key, it.value) }
                        header("referer", hyperSkillLoginUrl)
                        header("x-csrftoken", cookies.values.single())
                    }

                    val id = JSONObject(response)
                        .getJSONArray("profiles")
                        .getJSONObject(0)
                        .getString("id")
                    ResponseResult.Success(id)
                } catch (e: Exception) {
                    ResponseResult.Failure(e.message.toString())
                }
            }
        }
    }
}
