package com.mritunjay.talkdesk.data.remote

import com.mritunjay.talkdesk.data.dto.InProgressJobDto
import com.mritunjay.talkdesk.data.dto.JobClosedDto
import com.mritunjay.talkdesk.data.dto.JobDto

interface ApiService {
    suspend fun fetchJobs(): ApiResponse<JobDto>
    suspend fun acceptJob(id: String): ApiResponse<InProgressJobDto>
    suspend fun declineJob(id: String): ApiResponse<Unit>
    suspend fun updateIsUrgent(id: String, newState: Boolean): ApiResponse<Boolean>
    suspend fun updateIsAwaitingSupplies(id: String, newState: Boolean): ApiResponse<Boolean>
    suspend fun closeJob(id: String): ApiResponse<JobClosedDto>
    suspend fun escalateWithManager(id: String): ApiResponse<JobClosedDto>
}