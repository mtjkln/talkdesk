package com.mritunjay.talkdesk.domain.usecase

import com.mritunjay.talkdesk.domain.repository.JobRepository

class FetchJobs(private val jobRepository: JobRepository) {
    suspend operator fun invoke() = jobRepository.fetchJobs()
}