package com.mritunjay.talkdesk.data.respoitory

import com.mritunjay.talkdesk.data.mapper.DataToDomainMapper.toDomain
import com.mritunjay.talkdesk.data.remote.ApiResponse
import com.mritunjay.talkdesk.data.remote.ApiService
import com.mritunjay.talkdesk.domain.model.InProgressJobDomain
import com.mritunjay.talkdesk.domain.model.JobClosedDomain
import com.mritunjay.talkdesk.domain.model.JobDomain
import com.mritunjay.talkdesk.domain.repository.JobRepository

class JobRepositoryImpl(private val apiService: ApiService) : JobRepository {
    override suspend fun fetchJobs(): Result<JobDomain> {
        return when (val res = apiService.fetchJobs()) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data.toDomain())
            }
        }
    }

    override suspend fun acceptJob(id: String): Result<InProgressJobDomain> {
        return when (val res = apiService.acceptJob(id)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data.toDomain())
            }
        }
    }

    override suspend fun declineJob(id: String): Result<Unit> {
        return when (val res = apiService.declineJob(id)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data)
            }
        }
    }

    override suspend fun updateIsUrgent(
        id: String,
        newState: Boolean
    ): Result<Boolean> {
        return when (val res = apiService.updateIsUrgent(id, newState)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data)
            }
        }
    }

    override suspend fun updateIsAwaitingSupplies(
        id: String,
        newState: Boolean
    ): Result<Boolean> {
        return when (val res = apiService.updateIsAwaitingSupplies(id, newState)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data)
            }
        }
    }

    override suspend fun closeJob(id: String): Result<JobClosedDomain> {
        return when (val res = apiService.closeJob(id)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data.toDomain())
            }
        }
    }

    override suspend fun escalateWithManager(id: String): Result<JobClosedDomain> {
        return when (val res = apiService.escalateWithManager(id)) {
            is ApiResponse.Error -> {
                Result.failure(Exception(res.httpError.errorString))
            }

            is ApiResponse.Success -> {
                Result.success(res.data.toDomain())
            }
        }
    }
}