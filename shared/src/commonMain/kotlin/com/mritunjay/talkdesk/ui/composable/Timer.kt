package com.mritunjay.talkdesk.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mritunjay.talkdesk.ui.util.UIUtil
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Timer(elapsedAt: Long, pause: Boolean) {
    var elapsedAtLocal by remember { mutableStateOf(elapsedAt) }
    LaunchedEffect(elapsedAt, pause) {
        if (pause) {
            return@LaunchedEffect
        }
        while (true) {
            delay(1000.milliseconds)
            elapsedAtLocal++
        }
    }
    CustomText(UIUtil.formatElapsedAtToMMSS(elapsedAtLocal))
}