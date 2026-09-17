package com.mritunjay.talkdesk.domain.model


data class InProgressJobDomain(
    val job: JobDomain,
    val isUrgent: Boolean,
    val isAwaitingSupplies: Boolean,
    val startedAt: Long,
    val elapsedTime: Long? = null
)
