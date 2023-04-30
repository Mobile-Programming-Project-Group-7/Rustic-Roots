package com.example.rusticroots.pages

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
import com.example.rusticroots.model.data.Tables
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
        Surface(modifier = Modifier
            .fillMaxWidth()
            //.height(80.dp)
            ,
            color = MaterialTheme.colors.background) {
            //DurationPicker(1){}
            BookingPage()
        }
    }
}

@Composable
fun BookingPage() {
    var confirmed by remember { mutableStateOf(false) }
    val isItConfirmed: (Boolean) -> Unit = { confirmed = it}

    var tabIndex by remember { mutableStateOf(0) }
    val indexMe: (Int) -> Unit = { tabIndex = it }

    val tabs = listOf("Details", "Summary")
    Column {
        PageTop() //TODO please add functionality to the icon button
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
fun DetailsScreen(
    indexMe: (tabIndex: Int) -> Unit ,
    isItConfirmed: (confirmed: Boolean) -> Unit,
    vm: ReservationsViewModel = viewModel()
) {
    //vm.anonLogin()
    //vm.createBooking(1, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(11, 0)), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
    //vm.createBooking(2, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(11, 0)), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)))
    //vm.getAllBookings()
    if (vm._allTables.isEmpty()) vm.getAllTables()

    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val setDate: (LocalDate) -> Unit = { pickedDate = it }

    var pickedHour by remember { mutableStateOf(0) }
    val setHour: (Int) -> Unit = { pickedHour = it }

    var pickedTableList: List<Tables>? by remember { mutableStateOf(null) }
    val setTableList: (List<Tables>) -> Unit = { pickedTableList = it }

    var guests by remember { mutableStateOf(0) }
    val setGuest: (Int) -> Unit = { guests = it }

    var duration by remember { mutableStateOf(1) }
    val setDuration: (Int) -> Unit = { duration = it }

    Column{
        BookingDatePicker(setDate)
        Divider()
        BookingTimePicker(setHour, setTableList, pickedDate, pickedHour)
        Divider()
        GuestPicker(guests, setGuest)
        Divider()
        DurationPicker(duration, setDuration)
        Divider()
        TablePicker(pickedTableList)
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
fun DurationPicker(selected: Int, setNum: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .height(80.dp)
    ){
        ColumnTitle(title = "Duration")
        LazyRow(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = (1..3).toList(),
            ){
                Button(
                    colors = if (it == selected) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary) else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        setNum(it)
                    }
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "$it " + if (it == 1) "hour" else "hours"
                    )
                }
            }
        }
    }
}

@Composable
fun TablePicker(
    tableList: List<Tables>?,
    vm: ReservationsViewModel = viewModel()
){

}

@Composable
fun GuestPicker(selected: Int, setNum: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .height(80.dp)
    ){
        ColumnTitle(title = "Number of Guests")
        LazyRow(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = (1..8).toList(),
            ){
                Button(
                    colors = if (it == selected) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary) else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        setNum(it)
                    }
                ) {
                    Text(fontWeight = FontWeight.Bold,text = "$it")
                }
            }
        }
    }
}

/**
 * Time Picker
 * Updates values in parent function
 */
@Composable
fun BookingTimePicker(
    pickedHour: (Int) -> Unit,
    pickedTableList: (List<Tables>) -> Unit,
    pickedDate: LocalDate,
    selected: Int,
    vm: ReservationsViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .height(88.dp)
            .padding(horizontal = 8.dp)
    ) {
        ColumnTitle(title = "Hour")
        LazyRow(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (LocalDate.now() == pickedDate && LocalTime.now().hour >= 20)
                item { Text(
                    modifier = Modifier
                        .padding(horizontal = 36.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    text = "No Available Hours"
                )}
            else items(items = (if(LocalTime.now().hour > 11) LocalTime.now().hour..20 else 11..20).toList(),
            ){
                val list = vm.checkTableAvailability(it, date = pickedDate)
                Button(
                    colors = if (it == selected) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary) else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = list.isNotEmpty(),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        pickedHour(it)
                        pickedTableList(list)
                    }
                ) {
                    Text(fontWeight = FontWeight.Bold,text = "$it:00")
                }
            }
        }
    }
}

/**
 * Date Picker
 * Updates values in parent function
 */
@Composable
fun BookingDatePicker(setDate: (LocalDate) -> Unit) {
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
                it.isAfter(LocalDate.now().minusDays(1)) && it.dayOfWeek >= DayOfWeek.WEDNESDAY
            }
        ) {
            pickedDate = it
            setDate(it)
        }
    }
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
