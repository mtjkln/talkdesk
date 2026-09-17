package com.mritunjay.talkdesk.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.mritunjay.talkdesk.ui.viewmodel.JobUIState

@Composable
fun ClosedCard(closedJob: JobUIState.ClosedJob, onRemoveClosedJob: () -> Unit) {
    Column {
        JobDetails(closedJob.jobClosed.job.requestType, closedJob.jobClosed.job.roomNumber)
        CustomText("Reason for closing: ${closedJob.jobClosed.reasonForJobClosed.reasonLocalised}")
        CustomButton(onClick = { onRemoveClosedJob() }, text = "Closed")
    }
}