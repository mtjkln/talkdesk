package com.mritunjay.talkdesk.ui.util

object UIUtil {
    fun formatElapsedAtToMMSS(elapsedTime: Long): String {
        val sec = elapsedTime % 60
        val min = elapsedTime / 60
        val minString = min.toString().padStart(2, '0')
        val secString = sec.toString().padStart(2, '0')
        return "$minString:$secString"
    }

}