package com.example.rusticroots.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rusticroots.R

@Composable
fun ErrorScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 120.dp)
    ) {
        Icon(
            modifier = Modifier.size(250.dp, 250.dp),
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.error),
            tint = MaterialTheme.colors.error
        )
        Text(
            text = stringResource(R.string.error_message),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

}
