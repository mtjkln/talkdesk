package com.mritunjay.talkdesk

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform