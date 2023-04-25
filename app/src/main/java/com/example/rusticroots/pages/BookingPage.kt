package com.example.rusticroots.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rusticroots.R
import com.example.rusticroots.ui.theme.RusticRootsTheme
import com.example.rusticroots.viewmodel.ReservationsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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
    val vm: ReservationsViewModel = viewModel()
    vm.getAllBookings()
    if (vm._allTables.isEmpty()) vm.getAllTables()

    var confirmed by remember { mutableStateOf(false) }
    val isItConfirmed: (Boolean) -> Unit = { confirmed = it}

    var tabIndex by remember { mutableStateOf(0) }
    val indexMe: (Int) -> Unit = { tabIndex = it }

    val tabs = listOf("Details", "Summary")
    Column {
        PageTop() //TODO please add functionality to the iconbutton
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
fun DateScroll() {
    
}

@Composable
fun DetailsScreen(indexMe: (tabIndex: Int) -> Unit ,isItConfirmed: (confirmed: Boolean) -> Unit) {
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }

    Column{
        pickedDate = BookingDatePicker()
        Divider()
        BookingTimePicker()
        Divider()
        ColumnTitle(title = "Number of Guests")
        Divider()
        ColumnTitle(title = "Duration")
        Divider()
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(50),
            onClick = {
                isItConfirmed(true)
                indexMe(1)
            }
        ) {
            Text(fontWeight = FontWeight.Bold, text = "Continue")
        }
    }

}

@Composable
fun SummaryScreen() {
    Text(text = "title")
}

@Composable
fun BookingTimePicker() {
    val vm: ReservationsViewModel = viewModel()
    Column(
        modifier = Modifier
            .height(80.dp)
    ) {
        ColumnTitle(title = "Hour")
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = (11..22).toList()
            ){
                Text(text = it.toString())
            }
        }
    }
}

@Composable
fun BookingDatePicker(): LocalDate {
    val context = LocalContext.current
    var pickedDate by remember {
        mutableStateOf(
            if (LocalDate.now().dayOfWeek < DayOfWeek.WEDNESDAY)
                LocalDate.now().plusDays(DayOfWeek.WEDNESDAY.value.toLong() - LocalDate.now().dayOfWeek.value)
            else
                LocalDate.now()
        )
    }
    val formattedDate by remember {
        derivedStateOf { DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate) }
    }
    val dateDialogState = rememberMaterialDialogState()
    
    Column(
        modifier = Modifier
            .height(80.dp)
            .clickable {
                dateDialogState.show()
            },
    ) {
        ColumnTitle(title = "Date")
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                modifier = Modifier.scale(1.3f),
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Pick Date"
            )
            Text(
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                text = formattedDate.toString()
            )
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Set") {
                Toast.makeText(
                    context,
                    "Date set",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            yearRange = LocalDate.now().year..LocalDate.now().plusYears(1).year,
            initialDate = pickedDate,
            title = "Pick a date",
            allowedDateValidator = {
                it.isAfter(LocalDate.now()) && it.dayOfWeek >= DayOfWeek.WEDNESDAY
            }
        ) {
            pickedDate = it
        }
    }
    return pickedDate
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
