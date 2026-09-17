package com.mritunjay.talkdesk

import com.mritunjay.talkdesk.data.dto.InProgressJobDto
import com.mritunjay.talkdesk.data.dto.JobClosedDto
import com.mritunjay.talkdesk.data.dto.JobDto
import com.mritunjay.talkdesk.data.mapper.DataToDomainMapper.toDomain
import com.mritunjay.talkdesk.data.remote.ApiResponse
import com.mritunjay.talkdesk.data.remote.ApiService
import com.mritunjay.talkdesk.data.remote.FakeApiData
import com.mritunjay.talkdesk.data.remote.HttpError
import com.mritunjay.talkdesk.data.respoitory.JobRepositoryImpl
import com.mritunjay.talkdesk.di.JobUseCase
import com.mritunjay.talkdesk.domain.model.InProgressJobDomain
import com.mritunjay.talkdesk.domain.repository.JobRepository
import com.mritunjay.talkdesk.domain.usecase.AcceptJob
import com.mritunjay.talkdesk.domain.usecase.CloseJob
import com.mritunjay.talkdesk.domain.usecase.DeclineJob
import com.mritunjay.talkdesk.domain.usecase.EscalateWithManager
import com.mritunjay.talkdesk.domain.usecase.FetchJobs
import com.mritunjay.talkdesk.domain.usecase.UpdateIsAwaitingSupplies
import com.mritunjay.talkdesk.domain.usecase.UpdateIsUrgent
import com.mritunjay.talkdesk.ui.viewmodel.JobUIState
import com.mritunjay.talkdesk.ui.viewmodel.JobUIStateLoading
import com.mritunjay.talkdesk.ui.viewmodel.JobViewModel
import com.mritunjay.talkdesk.ui.viewmodel.PendingJobLoadingState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiServiceImplForTest(
    private val shouldFail: Boolean,
    private val deferFetchJob: CompletableDeferred<Unit>? = null,
    private val deferAcceptJob: CompletableDeferred<Unit>? = null,
    private val deferDeclineJob: CompletableDeferred<Unit>? = null,
    private val deferUpdateIsUrgent: CompletableDeferred<Unit>? = null,
    private val deferUpdateIsAwaitingSupplies: CompletableDeferred<Unit>? = null,
    private val deferCloseJob: CompletableDeferred<Unit>? = null,
) : ApiService {


    override suspend fun fetchJobs(): ApiResponse<JobDto> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferFetchJob?.await()
            ApiResponse.Success(FakeApiData.jobs[0])
        }
    }

    override suspend fun acceptJob(id: String): ApiResponse<InProgressJobDto> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferAcceptJob?.await()
            ApiResponse.Success(InProgressJobDto(FakeApiData.jobs[0], false, false, 0))
        }
    }

    override suspend fun declineJob(id: String): ApiResponse<Unit> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferDeclineJob?.await()
            ApiResponse.Success(Unit)
        }
    }

    override suspend fun updateIsUrgent(
        id: String,
        newState: Boolean
    ): ApiResponse<Boolean> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferUpdateIsUrgent?.await()
            ApiResponse.Success(newState)
        }
    }

    override suspend fun updateIsAwaitingSupplies(
        id: String,
        newState: Boolean
    ): ApiResponse<Boolean> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferUpdateIsAwaitingSupplies?.await()
            ApiResponse.Success(newState)
        }
    }

    override suspend fun closeJob(id: String): ApiResponse<JobClosedDto> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferCloseJob?.await()
            ApiResponse.Success(JobClosedDto(FakeApiData.jobs[0], "closed"))
        }
    }

    override suspend fun escalateWithManager(id: String): ApiResponse<JobClosedDto> {
        return if (shouldFail) {
            ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        } else {
            deferCloseJob?.await()
            ApiResponse.Success(JobClosedDto(FakeApiData.jobs[0], "escalated"))
        }
    }

}

const val STARTED_AT_TIME_STAMP = 0L

@OptIn(ExperimentalCoroutinesApi::class)
class JobViewModelTest {
    val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setDispatcher() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun resetDispatcher() {
        Dispatchers.resetMain()
    }

    fun getApiService(
        shouldFail: Boolean, deferFetchJob: CompletableDeferred<Unit>? = null,
        deferAcceptJob: CompletableDeferred<Unit>? = null,
        deferDeclineJob: CompletableDeferred<Unit>? = null,
        deferUpdateIsUrgent: CompletableDeferred<Unit>? = null,
        deferUpdateIsAwaitingSupplies: CompletableDeferred<Unit>? = null,
        deferCloseJob: CompletableDeferred<Unit>? = null,
    ): ApiService {
        return ApiServiceImplForTest(
            shouldFail,
            deferFetchJob,
            deferAcceptJob,
            deferDeclineJob,
            deferUpdateIsUrgent,
            deferUpdateIsAwaitingSupplies,
            deferCloseJob
        )
    }

    fun getRepository(
        shouldFail: Boolean, deferFetchJob: CompletableDeferred<Unit>? = null,
        deferAcceptJob: CompletableDeferred<Unit>? = null,
        deferDeclineJob: CompletableDeferred<Unit>? = null,
        deferUpdateIsUrgent: CompletableDeferred<Unit>? = null,
        deferUpdateIsAwaitingSupplies: CompletableDeferred<Unit>? = null,
        deferCloseJob: CompletableDeferred<Unit>? = null,
    ): JobRepository {
        return JobRepositoryImpl(
            getApiService(
                shouldFail,
                deferFetchJob,
                deferAcceptJob,
                deferDeclineJob,
                deferUpdateIsUrgent,
                deferUpdateIsAwaitingSupplies,
                deferCloseJob
            )
        )
    }

    fun getUseCases(jobRepository: JobRepository): JobUseCase {
        return JobUseCase(
            AcceptJob(jobRepository),
            DeclineJob(jobRepository),
            FetchJobs(jobRepository),
            EscalateWithManager(jobRepository),
            CloseJob(jobRepository),
            UpdateIsAwaitingSupplies(jobRepository),
            UpdateIsUrgent(jobRepository)
        )
    }


    @Test
    fun should_start_ideal_state() {
        val repository = getRepository(false)
        val jobViewModel = JobViewModel(getUseCases(repository))
        assertEquals(JobUIState.Ideal, jobViewModel.jobUIState.value)
    }

    @Test
    fun should_update_loading_state_when_fetch_job_is_invoked() = runTest {
        val deferredFetchJob = CompletableDeferred<Unit>()
        val repository = getRepository(shouldFail = false, deferFetchJob = deferredFetchJob)
        val jobViewModel = JobViewModel(getUseCases(repository))
        jobViewModel.fetchJob()
        advanceUntilIdle()
        assertEquals(JobUIState.Loading(JobUIStateLoading.FETCH_JOB), jobViewModel.jobUIState.value)
        deferredFetchJob.complete(Unit)
    }

    @Test
    fun should_update_to_pending_job_state_after_fetch_job_is_completed() = runTest {
        val repository = getRepository(shouldFail = false)
        val jobViewModel = JobViewModel(getUseCases(repository))
        jobViewModel.fetchJob()
        advanceUntilIdle()
        assertEquals(
            JobUIState.PendingJob(FakeApiData.jobs[0].toDomain()),
            jobViewModel.jobUIState.value
        )
    }

    @Test
    fun should_update_loading_for_pending_job_when_accepting() = runTest {
        val deferAcceptJob = CompletableDeferred<Unit>()
        val repository = getRepository(shouldFail = false, deferAcceptJob = deferAcceptJob)
        val jobViewModel = JobViewModel(getUseCases(repository))
        jobViewModel.fetchJob()
        jobViewModel.acceptJob(FakeApiData.jobs[0].id)
        advanceUntilIdle()
        assertEquals(
            JobUIState.PendingJob(
                FakeApiData.jobs[0].toDomain(),
                PendingJobLoadingState.ACCEPTING
            ), jobViewModel.jobUIState.value
        )
        deferAcceptJob.complete(Unit)
    }

    @Test
    fun should_update_to_in_progress_when_accepted_pending_job() = runTest {
        val repository = getRepository(shouldFail = false)
        val jobViewModel = JobViewModel(getUseCases(repository))
        jobViewModel.fetchJob()
        jobViewModel.acceptJob(FakeApiData.jobs[0].id)
        advanceUntilIdle()
        assertEquals(
            JobUIState.InProgressJob(
                InProgressJobDomain(
                    FakeApiData.jobs[0].toDomain(),
                    false,
                    false,
                    STARTED_AT_TIME_STAMP
                )
            ), jobViewModel.jobUIState.value
        )
    }

}