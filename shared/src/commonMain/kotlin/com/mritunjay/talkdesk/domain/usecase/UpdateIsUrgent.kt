package com.mritunjay.talkdesk.domain.usecase

import com.mritunjay.talkdesk.domain.repository.JobRepository

class UpdateIsUrgent(private val jobRepository: JobRepository) {
    suspend operator fun invoke(id: String, newState: Boolean) =
        jobRepository.updateIsUrgent(id, newState)
}