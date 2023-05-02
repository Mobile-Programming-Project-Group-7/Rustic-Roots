package com.example.rusticroots.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rusticroots.R
import com.example.rusticroots.model.data.PassBookingData
import com.example.rusticroots.model.data.Tables
import com.example.rusticroots.viewmodel.ReservationsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun BookingScreen(vm: ReservationsViewModel = viewModel()) {
    vm.getAllBookings()
    if (vm.allTables.isEmpty()) vm.getAllTables()

    var confirmed by remember { mutableStateOf(false) }
    val isItConfirmed: (Boolean) -> Unit = { confirmed = it}

    var tabIndex by remember { mutableStateOf(0) }
    val indexMe: (Int) -> Unit = { tabIndex = it }

    var booking: PassBookingData? by remember { mutableStateOf(null) }
    val setBooking: (PassBookingData) -> Unit = { booking = it }

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
            0 -> DetailsScreen(setBooking, indexMe, isItConfirmed)
            1 -> SummaryScreen(booking)
        }
    }
}
@Composable
fun SummaryScreen(passedVal: PassBookingData?, vm: ReservationsViewModel = viewModel()) {
    val timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    val formattedDate by remember {
        derivedStateOf { DateTimeFormatter.ofPattern("MMM dd yyyy").format(passedVal?.time_start?.toLocalDate()) }
    }

    val guests = passedVal?.guests.toString()

    val timeStart = passedVal?.time_start?.toLocalTime()?.format(timeFormat)
    val timeEnd = passedVal?.time_end?.toLocalTime()?.format(timeFormat)
    val formattedTime = "$timeStart - $timeEnd"

    val tables = mutableListOf<String>()
    passedVal?.table?.forEach { tables.add(it.tableID) }
    val formattedTables = tables.toString().replace("[", "").replace("table_","").replace("]","")

    Column {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 72.dp, bottomEnd = 72.dp))
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth()
                .height(72.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(

                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center, text = "Summary"
            )
        }
        Column(modifier = Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
        ) {

            CustomCard("Date", formattedDate)
            CustomCard(title = "Guests", value = guests)
            CustomCard(title = "Time", value = formattedTime)
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                ,elevation = 4.dp,
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ColumnTitle(title = "Table No.")
                        Text(
                            text = formattedTables,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                    passedVal?.table?.forEach {
                        CustomCard(
                            title = "Table ${it.tableID.replace("table_", "")}",
                            value = it.description
                        )
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                shape = RoundedCornerShape(50),
                onClick = {
                    passedVal?.table?.forEach {
                        vm.createBooking(
                            it.tableID,
                            passedVal.guests,
                            passedVal.time_start,
                            passedVal.time_end
                        )
                    }
                }
            ) {
                Text(fontWeight = FontWeight.Bold, text = "Confirm Booking")
            }
            Divider(modifier = Modifier.padding(vertical = 40.dp))
        }
    }
}

@Composable
fun CustomCard(title: String, value: String ) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
        ,elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            ColumnTitle(title = title)
            Text(
                textAlign = TextAlign.End,
                text = value,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
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
    setBooking: (booking: PassBookingData) -> Unit,
    indexMe: (tabIndex: Int) -> Unit ,
    isItConfirmed: (confirmed: Boolean) -> Unit,
    vm: ReservationsViewModel = viewModel()
) {
    val context = LocalContext.current

    var pickedDate by rememberSaveable {
        mutableStateOf(
            if (LocalDate.now().dayOfWeek < DayOfWeek.WEDNESDAY)
                    LocalDate.now().plusDays(DayOfWeek.WEDNESDAY.value.toLong() - LocalDate.now().dayOfWeek.value)
            else
                LocalDate.now()
        )
    }
    val setDate: (LocalDate) -> Unit = { pickedDate = it }

    var pickedHour by remember { mutableStateOf(0) }
    val setHour: (Int) -> Unit = { pickedHour = it }

    var pickedTableList: List<Tables>? by remember { mutableStateOf(null) }
    val setTableList: (List<Tables>) -> Unit = { pickedTableList = it }

    var maxGuests by remember { mutableStateOf(0) }
    val setMaxGuest: (Int) -> Unit = { maxGuests = it }

    var guests by remember { mutableStateOf(0) }
    val setGuest: (Int) -> Unit = { guests = it }

    var duration by rememberSaveable { mutableStateOf(1) }
    val setDuration: (Int) -> Unit = { duration = it }

    var table: List<Tables>? by remember { mutableStateOf(null) }
    val setTable: (List<Tables>) -> Unit = { table = it }

    val bool: Boolean = (pickedTableList != null && pickedHour != 0 && guests != 0)

    if(guests != 0) {
        pickedTableList?.let { it ->
            vm.tableSelector(it, guests, setTable)
        }
    }
    Column{
        BookingDatePicker(pickedDate, setDate)
        Divider()
        BookingTimePicker(setHour, setTableList, pickedDate, pickedHour, setMaxGuest, duration)
        Divider()
        DurationPicker(duration, setDuration, setGuest)
        Divider()
        GuestPicker(maxGuests, guests, setGuest)
        Divider()
        Button(
            colors =
                if (!bool)
                    ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                else
                    ButtonDefaults.buttonColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(50),
            onClick = {
                if (!bool) {
                    Toast.makeText(
                        context,
                        "Please select all fields to proceed",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    isItConfirmed(true)
                    indexMe(1)
                    table?.let {
                        setBooking(PassBookingData(
                                table = it,
                                guests = guests.toLong(),
                                time_start = LocalDateTime.of(
                                    pickedDate,
                                    LocalTime.of(pickedHour, 0)),
                                time_end = LocalDateTime.of(
                                    pickedDate,
                                    LocalTime.of(pickedHour, 0)
                                ).plusHours(duration.toLong())
                        ))
                    }
                }

            }
        ) {
            Text(fontWeight = FontWeight.Bold, text = "Continue")
        }
    }
}

@Composable
fun DurationPicker(
    selected: Int,
    setNum: (Int) -> Unit,
    setGuest: (Int) -> Unit
) {
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
                        setGuest(0) // Prevents app from crashing :)
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
fun GuestPicker(maxGuests: Int, selected: Int, setNum: (Int) -> Unit) {
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
                    enabled = maxGuests >= it,
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
    maxGuests: (Int) -> Unit,
    duration: Int,
    vm: ReservationsViewModel = viewModel()
) {
    var sum by remember {
        mutableStateOf(0)
    }
    val times = (
            if(LocalTime.now().hour > 11 && pickedDate == LocalDate.now())
                LocalTime.now().hour..20
            else
                11..20
    ).toList()

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
            else items(items = (times),
            ){
                val list by lazy { vm.checkTableAvailability(it, date = pickedDate, endIn = duration.toLong())}
                val num by lazy {
                    sum = 0
                    list.forEach { table ->
                        sum += table.seats.toInt()
                    }
                    sum
                }

                Button(
                    colors = if (it == selected) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary) else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = list.isNotEmpty(),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        pickedHour(it)
                        pickedTableList(list)
                        maxGuests(num)
                        list.forEach{t->Log.e("LIST OF TABLES TIME PICKER", t.toString())}
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
fun BookingDatePicker(pickedDate: LocalDate, setDate: (LocalDate) -> Unit) {
    val context = LocalContext.current
    var date by remember {
        mutableStateOf(pickedDate)
    }
    val formattedDate by remember {
        derivedStateOf { DateTimeFormatter.ofPattern("MMM dd yyyy").format(date) }
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
            setDate(it)
            date = it
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
                modifier = Modifier.height(160.dp)
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
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Table Reservation"
                )
                /*
                IconButton(onClick = { /*TODO navController.navigateUp()*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Go back"
                    )
                }
                //text here
                Box(modifier = Modifier) // Empty object
                */
            }
            Divider(color = Color.Gray)
        }
        //TODO if you want to add the logo back. Personally i think it looks better without
        /*
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

         */
    }
}
