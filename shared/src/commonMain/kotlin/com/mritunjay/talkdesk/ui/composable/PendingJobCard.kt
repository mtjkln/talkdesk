package com.mritunjay.talkdesk.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.mritunjay.talkdesk.ui.viewmodel.JobUIState
import com.mritunjay.talkdesk.ui.viewmodel.PendingJobLoadingState

@Composable
fun PendingJobCard(pendingJob: JobUIState.PendingJob, onAccept: () -> Unit, onDecline: () -> Unit) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
        JobDetails(requestType = pendingJob.job.requestType, roomNumber = pendingJob.job.roomNumber)
        Row {
            CustomButton(
                onClick = { onAccept() },
                text = "Accept",
                loading = pendingJob.pendingJobLoadingState == PendingJobLoadingState.ACCEPTING,
                disabled = pendingJob.pendingJobLoadingState != null
            )
            CustomButton(
                onClick = { onDecline() },
                text = "Decline",
                loading = pendingJob.pendingJobLoadingState == PendingJobLoadingState.DECLINING,
                disabled = pendingJob.pendingJobLoadingState != null
            )
        }
    }
}