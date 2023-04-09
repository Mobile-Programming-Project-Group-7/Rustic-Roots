package com.example.rusticroots
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rusticroots.model.data.MenuItem

@Composable
fun  DrawerHeader() {
    Box(modifier= Modifier.fillMaxWidth()){
        Image(
            painterResource(id= R.drawable.rustic_roots),"Rustic Roots",
            modifier = Modifier.size(200.dp).align(Alignment.TopStart))



    }
    
}
@Composable
fun DrawerBody(
    items:List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: androidx.compose.ui.text.TextStyle=androidx.compose.ui.text.TextStyle(fontSize=18.sp),
    onItemClick: (MenuItem) -> Unit

    ) {
    LazyColumn(modifier){
        items(items) {item ->
        Row(
            modifier =Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(item)

                }
                .padding(16.dp)
        ){
            Icon(imageVector=item.icon, contentDescription=item.contentDescription
            )
            Spacer(modifier= modifier.width(16.dp))
            Text(
                text=item.title,
                style = itemTextStyle,
                modifier = Modifier.weight(1f)
            )

        }

        }
    }

}
