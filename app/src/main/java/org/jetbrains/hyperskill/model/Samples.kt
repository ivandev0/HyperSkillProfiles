package org.jetbrains.hyperskill.model

val simpleProject = HyperSkillProject(
    id = 1000,
    name = "Simple Project",
    description = "Here goes small description of project. Here goes second sentence.",
    level = HyperSkillProjectDifficulty.Easy,
    rating = 4.3f,
    expectedTime = 12,
    topicsToLearn = 39
)

val simpleUser = HyperSkillUser(
    id = 1000,
    name = "Full Name",
    stats = HyperSkillUserStats(6, 0, 6),
    bio = "Here goes biography",
    avatarUrl = "https://ucarecdn.com/efff3079-1b03-4f5c-bbf2-dd2a8d9d49e5/-/crop/1706x1707/436,0/-/preview/",
    experience = null,
    country = "RU",
    languages = listOf("ru", "en")
)