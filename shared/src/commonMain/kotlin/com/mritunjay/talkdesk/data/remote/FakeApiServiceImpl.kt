package com.mritunjay.talkdesk.data.remote

import com.mritunjay.talkdesk.data.dto.InProgressJobDto
import com.mritunjay.talkdesk.data.dto.JobClosedDto
import com.mritunjay.talkdesk.data.dto.JobDto
import com.mritunjay.talkdesk.data.util.DataUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

const val API_DELAY_UPPER_BOUND_IN_SEC = 4
const val API_DELAY_LOWER_BOUND_IN_SEC = 2
const val SEC_TO_MS_FACTOR = 1000
const val COUNT_TO_FAIL_ESCALATE_WITH_MANAGER = 5

class FakeApiServiceImpl : ApiService {
    private var escalateWithManagerCount = 0
    override suspend fun fetchJobs(): ApiResponse<JobDto> {
        try {
            mimicApiDelay()
            return ApiResponse.Success(
                FakeApiData.jobs[DataUtil.getRandomNumberForRange(
                    0,
                    FakeApiData.jobs.size
                )]
            )
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun acceptJob(id: String): ApiResponse<InProgressJobDto> {
        try {
            mimicApiDelay()
            val job = getJobForId(id)
            return if (job == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(
                    InProgressJobDto(
                        job,
                        false,
                        false,
                        Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun declineJob(id: String): ApiResponse<Unit> {
        try {
            mimicApiDelay()
            return if (getJobForId(id) == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(Unit)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun updateIsUrgent(
        id: String,
        newState: Boolean
    ): ApiResponse<Boolean> {
        try {
            mimicApiDelay()
            return if (getJobForId(id) == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(newState)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun updateIsAwaitingSupplies(
        id: String,
        newState: Boolean
    ): ApiResponse<Boolean> {
        try {
            mimicApiDelay()
            return if (getJobForId(id) == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(newState)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun closeJob(id: String): ApiResponse<JobClosedDto> {
        try {
            mimicApiDelay()
            val job = getJobForId(id)
            return if (job == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(JobClosedDto(job, "closed"))
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun escalateWithManager(id: String): ApiResponse<JobClosedDto> {
        try {
            mimicApiDelay()
            if (++escalateWithManagerCount >= COUNT_TO_FAIL_ESCALATE_WITH_MANAGER) {
                escalateWithManagerCount = 0
                return ApiResponse.Error(HttpError.NOT_FOUND)
            }
            val job = getJobForId(id)
            return if (job == null) {
                ApiResponse.Error(HttpError.BAD_REQUEST)
            } else {
                ApiResponse.Success(JobClosedDto(job, "escalated"))
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return ApiResponse.Error(HttpError.INTERNAL_SERVER_ERROR)
        }
    }

    private suspend fun mimicApiDelay() {
        delay(
            (DataUtil.getRandomNumberForRange(
                API_DELAY_LOWER_BOUND_IN_SEC,
                API_DELAY_UPPER_BOUND_IN_SEC
            ) * SEC_TO_MS_FACTOR).milliseconds
        )
    }

    private fun getJobForId(id: String): JobDto? {
        return FakeApiData.jobs.find { it.id == id }
    }
}