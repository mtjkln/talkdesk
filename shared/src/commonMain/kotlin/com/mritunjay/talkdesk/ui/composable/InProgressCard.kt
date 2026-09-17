package com.mritunjay.talkdesk.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.mritunjay.talkdesk.ui.viewmodel.InProgressLoadingState
import com.mritunjay.talkdesk.ui.viewmodel.JobUIState

@Composable
fun InProgressCard(
    inProgressJob: JobUIState.InProgressJob,
    onEscalate: () -> Unit,
    onUpdateIsUrgent: (Boolean) -> Unit,
    onUpdateIsAwaitingSupplies: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    Column {
        JobDetails(
            inProgressJob.inProgressJob.job.requestType,
            inProgressJob.inProgressJob.job.roomNumber
        )
        Timer(
            inProgressJob.inProgressJob.elapsedTime ?: 0,
            inProgressJob.inProgressJob.isAwaitingSupplies
        )
        Row {
            CustomText("Urgent: ")
            Switch(inProgressJob.inProgressJob.isUrgent, {
                onUpdateIsUrgent(
                    it
                )
            })
        }
        Row {
            CustomText("Awaiting supplies : ")
            Switch(inProgressJob.inProgressJob.isAwaitingSupplies, {
                onUpdateIsAwaitingSupplies(
                    it
                )
            })
        }
        CustomButton(
            onClick = { onEscalate() },
            text = "Escalate With Manager",
            loading = inProgressJob.inProgressLoadingState == InProgressLoadingState.ESCALATING,
            disabled = inProgressJob.inProgressLoadingState != null
        )
        CustomButton(
            onClick = { onClose() },
            text = "Close",
            loading = inProgressJob.inProgressLoadingState == InProgressLoadingState.CLOSING,
            disabled = inProgressJob.inProgressLoadingState != null
        )
        if (inProgressJob.inProgressErrorState != null) {
            CustomText("Something went wrong please try again")
        }

    }
}

