package com.mritunjay.talkdesk.ui.composable

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    loading: Boolean = false,
    disabled: Boolean = false
) {
    Button(onClick = onClick, enabled = !loading && !disabled) {
        if (loading) {
            CircularProgressIndicator()
        } else {
            CustomText(text = text)
        }

    }
}