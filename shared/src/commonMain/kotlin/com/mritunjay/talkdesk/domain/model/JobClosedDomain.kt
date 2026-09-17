package com.mritunjay.talkdesk.domain.model


enum class ReasonForClosing(val reasonLocalised: String){
    ESCALATED("Escalated"),
    CLOSED("Closed"),
}

data class JobClosedDomain(val job: JobDomain, val reasonForJobClosed: ReasonForClosing)
