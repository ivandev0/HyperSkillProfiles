package org.jetbrains.hyperskill.model

import androidx.annotation.DrawableRes
import org.jetbrains.hyperskill.R

data class HyperSkillUser(
    val id: Long,
    val name: String,
    val stats: HyperSkillUserStats,
    val avatarUrl: String,
    val bio: String? = null,
    val experience: String? = null,
    val country: String? = null,
    val languages: List<String>? = null
)

data class HyperSkillUserStats(
    val projectsCompleted: Int, val tracksCompleted: Int, val badgesReceived: Int
)

// TODO load other images
enum class HyperSkillProjectDifficulty(@DrawableRes val imageId: Int) {
    Easy(R.drawable.ic_easy_icon), Medium(0), Hard(0), Challenging(0)
}

data class HyperSkillProject(
    val id: Long,
    val name: String,
    val description: String,
    val level: HyperSkillProjectDifficulty,
    val rating: Float,
    val expectedTime: Int,
    val topicsToLearn: Int
)