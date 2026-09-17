package com.mritunjay.talkdesk.data.remote

import com.mritunjay.talkdesk.data.dto.JobDto

object FakeApiData {
    val jobs = listOf(
        JobDto("1234", "room_service", "123"),
        JobDto("1235", "house_keeping", "321"),
        JobDto("1236", "maintenance", "451")
    )
}