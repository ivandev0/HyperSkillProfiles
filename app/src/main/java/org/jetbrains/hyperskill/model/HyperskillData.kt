package org.jetbrains.hyperskill.model

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

class HyperSkillProject {
}