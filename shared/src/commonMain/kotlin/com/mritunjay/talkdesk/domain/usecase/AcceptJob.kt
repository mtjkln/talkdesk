package com.mritunjay.talkdesk.domain.usecase

import com.mritunjay.talkdesk.domain.repository.JobRepository

class AcceptJob(private val jobRepository: JobRepository) {
    suspend operator fun invoke(id: String) = jobRepository.acceptJob(id)
}