package com.example.rusticroots.pages

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen() {
    val isDarkModeOn = isSystemInDarkTheme()

    MaterialTheme(
        colors = if (isDarkModeOn) darkColors() else lightColors()
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Settings") }
                )
            }
        )
        {
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Text(text = "Dark mode")
                    Switch(
                        checked = isDarkModeOn,
                        onCheckedChange = {
                            AppCompatDelegate.setDefaultNightMode(
                                if (it) AppCompatDelegate.MODE_NIGHT_YES
                                else AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Text(text = "Notifications")
                    Switch(
                        checked = true,
                        onCheckedChange = { /* TODO */ }
                    )
                }
            }
        }
    }}}

