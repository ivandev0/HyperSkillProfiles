package org.jetbrains.hyperskill.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.hyperskill.model.HyperSkillUser
import org.json.JSONObject


object ApiService {
    private const val hyperSkillUrl = "https://hyperskill.org/"
    private const val hyperSkillApiUrl = "${hyperSkillUrl}api/"

    fun getUserData(id: String): HyperSkillUser {
        return runBlocking {
            val response = HttpClient().use { httpClient ->
                httpClient.get<String>(hyperSkillApiUrl + "users/" + id)
            }
            // TODO handle errors
            val json = JSONObject(response).getJSONArray("users").getJSONObject(0)
            HyperSkillUser(
                json.getLong("id"),
                json.getString("fullname"),
                json.getString("avatar"),
                json.getString("bio").takeIf { it.isNotEmpty() }
            )
        }
    }

    private fun getCookies(): Map<String, String> {
        return runBlocking {
            val response = HttpClient().use { httpClient ->
                httpClient.get<HttpResponse>(hyperSkillUrl + "login?next=%2Ftracks").headers["set-cookie"]
            }

            response!!.split(";").map {
                val cookie = it.split("=")
                cookie[0] to cookie[1]
            }.toMap()
        }
    }

    fun login(email: String, password: String): String {
        val cookies = getCookies().filter { it.key == "csrftoken" }

        return runBlocking {
            HttpClient().use { httpClient ->
                httpClient.post<HttpResponse>(hyperSkillApiUrl + "profiles/login") {
                    contentType(ContentType.Application.Json)
                    body = "{\"email\":\"$email\",\"password\":\"$password\"}"
                    cookies.forEach { cookie(it.key, it.value) }
                    header("referer", "https://hyperskill.org/login?next=%2Ftracks")
                    header("x-csrftoken", cookies.values.single())
                }.receive()
            }
        }
    }
}