package com.mritunjay.talkdesk.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mritunjay.talkdesk.domain.model.RequestType

@Composable
fun JobDetails(requestType: RequestType, roomNumber: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomText("Room Number: $roomNumber")
        CustomText(requestType.localisedName)
    }
}