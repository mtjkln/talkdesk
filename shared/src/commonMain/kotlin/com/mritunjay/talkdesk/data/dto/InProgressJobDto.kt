package com.mritunjay.talkdesk.data.dto

data class InProgressJobDto(
    val job: JobDto,
    val isUrgent: Boolean,
    val isAwaitingSupplies: Boolean,
    val startedAt: Long,
    val elapsedTime: Long? = null
)