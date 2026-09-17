package com.mritunjay.talkdesk.domain.model

enum class RequestType(val requestType: String, val localisedName: String) {
    HOUSE_KEEPING("house_keeping", "House keeping"),
    MAINTENANCE("maintenance", "Maintenance"),
    ROOM_SERVICE("room_service", "Room service"),
    UNKNOWN("unknown", "Unknown")
}

data class JobDomain(val id: String, val requestType: RequestType, val roomNumber: String)
