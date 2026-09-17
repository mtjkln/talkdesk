package com.mritunjay.talkdesk.ui.viewmodel

import com.mritunjay.talkdesk.domain.model.InProgressJobDomain
import com.mritunjay.talkdesk.domain.model.JobClosedDomain
import com.mritunjay.talkdesk.domain.model.JobDomain

enum class JobUIStateLoading {
    FETCH_JOB
}

enum class JobUIStateError {
    FETCH_JOB
}

enum class PendingJobLoadingState {
    ACCEPTING,
    DECLINING
}

enum class PendingJobErrorState {
    ACCEPTING,
    DECLINING
}

enum class InProgressLoadingState {
    UPDATING_IS_URGENT,
    UPDATING_IS_AWAITING_FOR_SUPPLIES,
    ESCALATING,
    CLOSING
}

enum class InProgressErrorState {
    UPDATING_IS_URGENT,
    UPDATING_IS_AWAITING_FOR_SUPPLIES,
    ESCALATING,
    CLOSING
}

sealed interface JobUIState {
    data object Ideal : JobUIState
    data class Loading(val jobUIStateLoading: JobUIStateLoading) : JobUIState
    data class Error(val jobUIStateError: JobUIStateError) : JobUIState
    data class PendingJob(
        val job: JobDomain,
        val pendingJobLoadingState: PendingJobLoadingState? = null,
        val pendingJobErrorState: PendingJobErrorState? = null
    ) : JobUIState

    data class InProgressJob(
        val inProgressJob: InProgressJobDomain,
        val inProgressLoadingState: InProgressLoadingState? = null,
        val inProgressErrorState: InProgressErrorState? = null
    ) : JobUIState

    data class ClosedJob(val jobClosed: JobClosedDomain) : JobUIState
}