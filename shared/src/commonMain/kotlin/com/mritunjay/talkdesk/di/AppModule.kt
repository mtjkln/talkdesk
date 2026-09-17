package com.mritunjay.talkdesk.di

import com.mritunjay.talkdesk.data.remote.FakeApiServiceImpl
import com.mritunjay.talkdesk.data.respoitory.JobRepositoryImpl
import com.mritunjay.talkdesk.domain.usecase.AcceptJob
import com.mritunjay.talkdesk.domain.usecase.CloseJob
import com.mritunjay.talkdesk.domain.usecase.DeclineJob
import com.mritunjay.talkdesk.domain.usecase.EscalateWithManager
import com.mritunjay.talkdesk.domain.usecase.FetchJobs
import com.mritunjay.talkdesk.domain.usecase.UpdateIsAwaitingSupplies
import com.mritunjay.talkdesk.domain.usecase.UpdateIsUrgent

data class JobUseCase(
    val acceptJob: AcceptJob,
    val declineJob: DeclineJob,
    val fetchJobs: FetchJobs,
    val escalateWithManager: EscalateWithManager,
    val closeJob: CloseJob,
    val updateIsAwaitingSupplies: UpdateIsAwaitingSupplies,
    val updateIsUrgent: UpdateIsUrgent
)

object AppModule {
    val apiService = FakeApiServiceImpl()
    val jobRepository = JobRepositoryImpl(apiService)
    val jobUseCase = JobUseCase(
        AcceptJob(jobRepository),
        DeclineJob(jobRepository),
        FetchJobs(jobRepository),
        EscalateWithManager(jobRepository),
        CloseJob(jobRepository),
        UpdateIsAwaitingSupplies(jobRepository),
        UpdateIsUrgent(jobRepository)
    )

}