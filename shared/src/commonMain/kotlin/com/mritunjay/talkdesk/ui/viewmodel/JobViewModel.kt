package com.mritunjay.talkdesk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mritunjay.talkdesk.di.JobUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class JobViewModel(private val jobUseCase: JobUseCase) : ViewModel() {
    private val _jobUIState = MutableStateFlow<JobUIState>(JobUIState.Ideal)
    val jobUIState = _jobUIState.asStateFlow()
    fun fetchJob() {
        viewModelScope.launch {
            _jobUIState.value = JobUIState.Loading(JobUIStateLoading.FETCH_JOB)
            val res = jobUseCase.fetchJobs()
            res.fold(onSuccess = {
                _jobUIState.value = JobUIState.PendingJob(it)
            }, onFailure = {
                JobUIState.Error(JobUIStateError.FETCH_JOB)
            })
        }
    }

    fun acceptJob(id: String) {
        viewModelScope.launch {
            updateLoadingForPendingJob(PendingJobLoadingState.ACCEPTING)
            val res = jobUseCase.acceptJob(id)
            res.fold(onSuccess = {
                _jobUIState.value = JobUIState.InProgressJob(it)
            }, onFailure = {
                updateErrorForPendingJob(PendingJobErrorState.ACCEPTING)
            })
        }
    }

    fun declineJob(id: String) {
        viewModelScope.launch {
            updateLoadingForPendingJob(PendingJobLoadingState.DECLINING)
            val res = jobUseCase.declineJob(id)
            res.fold(onSuccess = {
                _jobUIState.value = JobUIState.Ideal
            }, onFailure = {
                updateErrorForPendingJob(PendingJobErrorState.DECLINING)
            })
        }
    }

    fun escalateWithManager(id: String) {
        viewModelScope.launch {
            updateLoadingForInProgressJob(InProgressLoadingState.ESCALATING)
            val res = jobUseCase.escalateWithManager(id)
            res.fold(onSuccess = {
                _jobUIState.value = JobUIState.ClosedJob(it)
            }, onFailure = {
                updateErrorForInProgressJob(InProgressErrorState.ESCALATING)
            })
        }
    }

    fun updateIsAwaitingSupplies(id: String, newState: Boolean) {
        viewModelScope.launch {
            updateLoadingForInProgressJob(InProgressLoadingState.UPDATING_IS_AWAITING_FOR_SUPPLIES)
            val res = jobUseCase.updateIsAwaitingSupplies(id, newState)
            res.fold(onSuccess = {
                _jobUIState.update {
                    if (it is JobUIState.InProgressJob) {
                        val newElapsedTime =
                            (it.inProgressJob.elapsedTime ?: 0) + ((Clock.System.now()
                                .toEpochMilliseconds() - it.inProgressJob.startedAt) / 1000)
                        return@update it.copy(
                            inProgressJob = it.inProgressJob.copy(
                                isAwaitingSupplies = newState,
                                elapsedTime = newElapsedTime
                            ),
                            inProgressErrorState = null,
                            inProgressLoadingState = null
                        )
                    }
                    it
                }
            }, onFailure = {
                updateErrorForInProgressJob(InProgressErrorState.UPDATING_IS_AWAITING_FOR_SUPPLIES)
            })
        }
    }

    fun updateIsUrgent(id: String, newState: Boolean) {
        viewModelScope.launch {
            updateLoadingForInProgressJob(InProgressLoadingState.UPDATING_IS_URGENT)
            val res = jobUseCase.updateIsUrgent(id, newState)
            res.fold(onSuccess = {
                _jobUIState.update {
                    if (it is JobUIState.InProgressJob) {
                        return@update it.copy(
                            inProgressJob = it.inProgressJob.copy(isUrgent = newState),
                            inProgressErrorState = null,
                            inProgressLoadingState = null
                        )
                    }
                    it
                }
            }, onFailure = {
                updateErrorForInProgressJob(InProgressErrorState.UPDATING_IS_URGENT)
            })
        }
    }

    fun closeJob(id: String) {
        viewModelScope.launch {
            updateLoadingForInProgressJob(InProgressLoadingState.CLOSING)
            val res = jobUseCase.closeJob(id)
            res.fold(onSuccess = {
                _jobUIState.value = JobUIState.ClosedJob(it)
            }, onFailure = {
                updateErrorForInProgressJob(InProgressErrorState.CLOSING)
            })
        }
    }

    fun goIdeal() {
        _jobUIState.value = JobUIState.Ideal
    }

    private fun updateLoadingForPendingJob(pendingJobLoadingState: PendingJobLoadingState) {
        _jobUIState.update {
            if (it is JobUIState.PendingJob) {
                return@update it.copy(
                    pendingJobLoadingState = pendingJobLoadingState,
                    pendingJobErrorState = null
                )
            }
            it
        }
    }

    private fun updateErrorForPendingJob(pendingJobErrorState: PendingJobErrorState) {
        _jobUIState.update {
            if (it is JobUIState.PendingJob) {
                return@update it.copy(
                    pendingJobLoadingState = null,
                    pendingJobErrorState = pendingJobErrorState
                )
            }
            it
        }
    }

    private fun updateLoadingForInProgressJob(inProgressLoadingState: InProgressLoadingState) {
        _jobUIState.update {
            if (it is JobUIState.InProgressJob) {
                return@update it.copy(
                    inProgressLoadingState = inProgressLoadingState,
                    inProgressErrorState = null
                )
            }
            it
        }
    }

    private fun updateErrorForInProgressJob(inProgressErrorState: InProgressErrorState) {
        _jobUIState.update {
            if (it is JobUIState.InProgressJob) {
                return@update it.copy(
                    inProgressLoadingState = null,
                    inProgressErrorState = inProgressErrorState
                )
            }
            it
        }
    }

}