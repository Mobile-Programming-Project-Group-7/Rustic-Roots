package com.example.rusticroots.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rusticroots.R
import com.example.rusticroots.ui.theme.RusticRootsTheme


@Preview(showBackground = true)
@Composable
fun BookingPreview() {
    RusticRootsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
           BookingPage()
        }
    }
}

@Composable
fun BookingPage() {
    var confirmed by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Details", "Summary")
    Column {
        PageTop()
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.height(72.dp),
            backgroundColor = MaterialTheme.colors.background,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = MaterialTheme.colors.primary,
                    height = TabRowDefaults.IndicatorHeight * 1.3f
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        text = title
                    )},
                    selected = tabIndex == index,
                    onClick = {
                        if (confirmed) tabIndex = index
                    }
                )
            }
        }
        val isItConfirmed: (Boolean) -> Unit = { confirmed = it}
        val indexMe: (Int) -> Unit = { tabIndex = it }
        when (tabIndex) {
            0 -> DetailsScreen(indexMe, isItConfirmed)
            1 -> SummaryScreen()
        }
    }
}

@Composable
fun ColumnTitle(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        text = title
    )
}

@Composable
fun DetailsScreen(indexMe: (tabIndex: Int) -> Unit ,isItConfirmed: (confirmed: Boolean) -> Unit) {
    Column {
        ColumnTitle(title = "Date")
        Divider()
        ColumnTitle(title = "Hour")
        Divider()
        ColumnTitle(title = "Number of Guests")
        Divider()
        ColumnTitle(title = "Duration")
        Divider()
        Button(onClick = {
            isItConfirmed(true)
            indexMe(1)
        }) {
            Text(text = "Continue")
        }
    }

}

@Composable
fun SummaryScreen() {
    Text(text = "title")
}

/**
 * Contains Box(Background Image + Table Reservation) + Logo
 */
@Composable
fun PageTop() {
    Box {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            // BackGround Image
            Image(
                painter = painterResource(id = R.drawable.ingredients),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.height(220.dp)
            )
            // Cancel button + Table Reservation text
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 72.dp, topEnd = 72.dp))
                    .background(MaterialTheme.colors.background)

            ) {
                IconButton(onClick = { /*TODO navController.navigateUp()*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Go back"
                    )
                }
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Table Reservation"
                )
                Box(modifier = Modifier) // Empty object
            }
            Divider(color = Color.Gray)
        }
        //Logo Container
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(color = Color.Black.copy(alpha = 0.9f))
        ) {
            Image(
                painter = painterResource(R.drawable.rustic_roots),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.scale(1.4f)
            )
        }
    }
}
