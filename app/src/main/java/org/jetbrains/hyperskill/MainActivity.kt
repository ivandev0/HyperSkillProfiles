package org.jetbrains.hyperskill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import org.jetbrains.hyperskill.model.HyperSkillProject
import org.jetbrains.hyperskill.model.HyperSkillUser
import org.jetbrains.hyperskill.model.HyperSkillUserStats
import org.jetbrains.hyperskill.model.simpleProject
import org.jetbrains.hyperskill.network.ApiService
import org.jetbrains.hyperskill.ui.theme.HyperskillProfilesTheme
import java.util.*

@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginPage(navController)
                }
                composable(
                    "profile/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { ProfileInfo(ApiService.getUserData(it.arguments!!.getString("userId")!!)) }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun LoginPage(navController: NavController = rememberNavController()) {
    HyperskillProfilesTheme {
        BoxWithConstraints(modifier = Modifier
            .fillMaxSize()
            .background(Color(0, 123, 255))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.hyperskill),
                    contentDescription = "HyperSkill logo",
                    modifier = Modifier.size(300.dp)
                )
                val email = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email.value,
                    label = { Text(text = "Enter Email") },
                    onValueChange = { email.value = it }
                )
                Spacer(Modifier.size(16.dp))
                OutlinedTextField(
                    value = password.value,
                    label = { Text(text = "Enter Password") },
                    onValueChange = { password.value = it }
                )
                Spacer(Modifier.size(16.dp))
                Button(onClick = {
                    val id = ApiService.login(email.value, password.value)
                    navController.navigate("profile/$id")
                }) { Text(text = "Login", color = Color.Black) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val url = "https://ucarecdn.com/efff3079-1b03-4f5c-bbf2-dd2a8d9d49e5/-/crop/1706x1707/436,0/-/preview/"
    ProfileInfo(user = HyperSkillUser(
        123, "Vladimir Klimov",
        avatarUrl = url,
        bio = "Product Data Analyst, Bioinformatist, Zoologist, Molecular Biologist",
        country = "RU",
        languages = listOf("ru", "en"),
        stats = HyperSkillUserStats(6, 0, 6)
    ))
}

@ExperimentalCoilApi
@Composable
fun ProfileInfo(user: HyperSkillUser) {
    HyperskillProfilesTheme {
        BoxWithConstraints(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(user, this@BoxWithConstraints.maxHeight * 0.4f)
                StatsBlock(stats = user.stats)
                BioBlock(user = user)

                ProjectsBlock(listOf(simpleProject, simpleProject))
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ProfileHeader(user: HyperSkillUser, height: Dp) {
    val blueBackground = Color(12, 24, 159)
    Column(
        modifier = Modifier
            .background(color = blueBackground)
            .height(height)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(user.avatarUrl),
            contentDescription = "User profile picture",
            modifier = Modifier
                .size(height * 0.5f)
                .clip(CircleShape)
        )
        Text(
            user.name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun StatsBlock(stats: HyperSkillUserStats) {
    @Composable
    fun RowScope.Block(@DrawableRes image: Int, count: Int, name: String, leftPadding: Boolean) {
        val textColor = Color(139, 145, 159)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(start = if (leftPadding) 10.dp else 0.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp),
                    painter = painterResource(id = image),
                    contentDescription = name
                )
                Column {
                    Text(text = count.toString())
                    Text(text = name, fontSize = 12.sp, color = textColor)
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .offset(y = (-50).dp)
            .height(100.dp)
            .padding(top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Block(
            image = R.drawable.ic_projects_completed,
            count = stats.projectsCompleted,
            name = "Projects",
            leftPadding = false
        )
        Block(
            image = R.drawable.ic_tracks_completed,
            count = stats.tracksCompleted,
            name = "Tracks",
            leftPadding = true
        )
        Block(
            image = R.drawable.ic_badges_received,
            count = stats.badgesReceived,
            name = "Badges",
            leftPadding = true
        )
    }
}

@Composable
inline fun BoxRowWithContent(content: @Composable BoxScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-50).dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f)
                .fillMaxHeight()
                .padding(top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            content = content
        )
    }
}

@Composable
fun BioBlock(user: HyperSkillUser) {
    val enLocale = Locale("en")
    val modifierWithPadding = Modifier.padding(bottom = 6.dp)

    BoxRowWithContent {
        Column {
            if (user.country != null) {
                val country = Locale("", user.country).getDisplayCountry(enLocale)
                Text(text = "Lives in $country", modifier = Modifier.padding(bottom = 4.dp))
            }

            if (user.languages != null) {
                Text(
                    text = "Speaks " + user.languages.joinToString { Locale(it).getDisplayLanguage(enLocale) },
                    modifier = modifierWithPadding
                )
            }

            if (user.bio != null) {
                Text(text = "Bio", fontWeight = FontWeight.Bold, modifier = modifierWithPadding)
                Text(text = user.bio, modifier = modifierWithPadding)
            } else {
                Text(
                    text = "Share something about yourself",
                    modifier = modifierWithPadding,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            }

            if (user.experience != null) {
                Text(text = "Experience", fontWeight = FontWeight.Bold, modifier = modifierWithPadding)
                Text(text = user.experience, modifier = modifierWithPadding)
            } else {
                Text(
                    text = "Share something about your experience",
                    modifier = modifierWithPadding,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ProjectsBlock(projects: List<HyperSkillProject>) {
    @Composable
    fun DrawIcon(@DrawableRes id: Int) {
        Image(
            modifier = Modifier
                .size(25.dp)
                .padding(5.dp),
            painter = painterResource(id = id),
            contentDescription = null,
        )
    }

    @Composable
    fun Block(project: HyperSkillProject) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .padding(end = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(245,247,255)),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row {
                    DrawIcon(id = project.level.imageId)
                    Text(text = project.name)
                }
                Spacer(modifier = Modifier.size(16.dp))

                Row {
                    Text(text = project.description)
                }
                Spacer(modifier = Modifier.size(16.dp))

                Row {
                    DrawIcon(id = R.drawable.ic_star_icon)
                    Text(text = project.rating.toString())
                    DrawIcon(id = R.drawable.ic_easy_icon)
                    Text(text = project.level.name)
                    DrawIcon(id = R.drawable.ic_hour_icon)
                    Text(text = "${project.rating} hours")
                    DrawIcon(id = R.drawable.ic_book_icon)
                    Text(text = "${project.topicsToLearn} topics")
                }
            }
        }
    }

    if (projects.isEmpty()) return

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .offset(y = (-50).dp)
            .height(150.dp)
            .padding(top = 5.dp, bottom = 5.dp),
        state = rememberLazyListState(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(projects.size) { Block(projects[it]) }
    }
}
