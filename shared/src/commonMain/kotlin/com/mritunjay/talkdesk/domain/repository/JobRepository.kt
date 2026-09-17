package com.mritunjay.talkdesk.domain.repository

import com.mritunjay.talkdesk.domain.model.InProgressJobDomain
import com.mritunjay.talkdesk.domain.model.JobClosedDomain
import com.mritunjay.talkdesk.domain.model.JobDomain


interface JobRepository {
    suspend fun fetchJobs(): Result<JobDomain>
    suspend fun acceptJob(id: String): Result<InProgressJobDomain>
    suspend fun declineJob(id: String): Result<Unit>
    suspend fun updateIsUrgent(id: String, newState: Boolean): Result<Boolean>
    suspend fun updateIsAwaitingSupplies(id: String, newState: Boolean): Result<Boolean>
    suspend fun closeJob(id: String): Result<JobClosedDomain>
    suspend fun escalateWithManager(id: String): Result<JobClosedDomain>
}