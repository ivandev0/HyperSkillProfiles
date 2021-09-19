package org.jetbrains.hyperskill

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val defaultRowModifier = Modifier
    .fillMaxWidth(fraction = 0.9f)
    .offset(y = (-50).dp)
    .padding(top = 5.dp, bottom = 5.dp)

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
}