package com.mritunjay.talkdesk.domain.usecase

import com.mritunjay.talkdesk.domain.repository.JobRepository

class UpdateIsAwaitingSupplies(private val jobRepository: JobRepository) {
    suspend operator fun invoke(id: String, newState: Boolean) = jobRepository.updateIsAwaitingSupplies(id,newState)
}