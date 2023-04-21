package com.example.rusticroots

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Job

@Composable
fun NavBar(onNavigationIconClick: () -> Job) {
    TopAppBar(
        title = {
            Text(text= stringResource(id= R.string.app_name))
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(
                onClick={},
                modifier=Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f) ){
                Icon(imageVector=Icons.Default.Menu,
                    contentDescription= "Toggle drawer"
                )

            }
        }




    ) 
    
}
