package com.mritunjay.talkdesk.data.mapper

import com.mritunjay.talkdesk.data.dto.InProgressJobDto
import com.mritunjay.talkdesk.data.dto.JobClosedDto
import com.mritunjay.talkdesk.data.dto.JobDto
import com.mritunjay.talkdesk.domain.model.InProgressJobDomain
import com.mritunjay.talkdesk.domain.model.JobClosedDomain
import com.mritunjay.talkdesk.domain.model.JobDomain
import com.mritunjay.talkdesk.domain.model.ReasonForClosing
import com.mritunjay.talkdesk.domain.model.RequestType

object DataToDomainMapper {
    fun JobDto.toDomain(): JobDomain {
        val mappedRequestType = when (requestType) {
            "room_service" -> RequestType.ROOM_SERVICE
            "maintenance" -> RequestType.MAINTENANCE
            "house_keeping" -> RequestType.HOUSE_KEEPING
            else -> RequestType.UNKNOWN
        }
        return JobDomain(requestType = mappedRequestType, id = id, roomNumber = roomNumber)
    }

    fun InProgressJobDto.toDomain(): InProgressJobDomain {
        return InProgressJobDomain(
            job = job.toDomain(),
            isUrgent,
            isAwaitingSupplies,
            startedAt,
            elapsedTime
        )
    }

    fun JobClosedDto.toDomain(): JobClosedDomain {
        val mappedReasonForJobClosed = when (reasonForJobClosed) {
            "escalated" -> ReasonForClosing.ESCALATED
            "closed" -> ReasonForClosing.CLOSED
            else -> ReasonForClosing.CLOSED
        }
        return JobClosedDomain(job = job.toDomain(), mappedReasonForJobClosed)
    }
}