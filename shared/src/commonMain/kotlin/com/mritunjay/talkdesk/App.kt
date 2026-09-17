package com.mritunjay.talkdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mritunjay.talkdesk.di.AppModule
import com.mritunjay.talkdesk.ui.composable.ClosedCard
import com.mritunjay.talkdesk.ui.composable.CustomButton
import com.mritunjay.talkdesk.ui.composable.CustomText
import com.mritunjay.talkdesk.ui.composable.InProgressCard
import com.mritunjay.talkdesk.ui.composable.PendingJobCard
import com.mritunjay.talkdesk.ui.viewmodel.JobUIState
import com.mritunjay.talkdesk.ui.viewmodel.JobViewModel

@Composable
@Preview
fun App(
    jobViewModel: JobViewModel = viewModel {
        JobViewModel(AppModule.jobUseCase)
    }
) {
    val jobUIState by jobViewModel.jobUIState.collectAsState()
    MaterialTheme {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (jobUIState) {
                is JobUIState.Ideal -> {
                    CustomButton(onClick = { jobViewModel.fetchJob() }, text = "Fetch Job")
                }

                is JobUIState.Loading -> {
                    CircularProgressIndicator()
                }

                is JobUIState.PendingJob -> {
                    PendingJobCard(
                        pendingJob = (jobUIState as JobUIState.PendingJob),
                        onAccept = { jobViewModel.acceptJob((jobUIState as JobUIState.PendingJob).job.id) },
                        onDecline = { jobViewModel.declineJob((jobUIState as JobUIState.PendingJob).job.id) })

                }

                is JobUIState.InProgressJob -> {
                    val inProgressJob = (jobUIState as JobUIState.InProgressJob)
                    InProgressCard(inProgressJob, {
                        jobViewModel.escalateWithManager(inProgressJob.inProgressJob.job.id)

                    }, {
                        jobViewModel.updateIsUrgent(inProgressJob.inProgressJob.job.id, it)
                    }, {
                        jobViewModel.updateIsAwaitingSupplies(
                            inProgressJob.inProgressJob.job.id,
                            it
                        )

                    }, { jobViewModel.closeJob(inProgressJob.inProgressJob.job.id) })


                }

                is JobUIState.ClosedJob -> {
                    val closedJob = (jobUIState as JobUIState.ClosedJob)
                    ClosedCard(closedJob, {
                        jobViewModel.goIdeal()
                    })
                }

                is JobUIState.Error -> {
                    CustomText("Something went wrong")

                }

            }
        }
    }
}