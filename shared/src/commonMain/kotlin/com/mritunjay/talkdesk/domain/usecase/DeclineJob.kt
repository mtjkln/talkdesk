package com.mritunjay.talkdesk.domain.usecase

import com.mritunjay.talkdesk.domain.repository.JobRepository

class DeclineJob (private val jobRepository: JobRepository) {
    suspend operator fun invoke(id: String) = jobRepository.declineJob(id)
}