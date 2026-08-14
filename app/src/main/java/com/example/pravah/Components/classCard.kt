package com.example.pravah.Components

import android.service.autofill.OnClickAction
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pravah.model.RoomModel

@Composable
fun cardComposable(
    room: RoomModel
){
        ElevatedCard(elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .padding(10.dp)
                .height(20.dp)
                .fillMaxHeight(0.2f)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            onClick = {},
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )) {
            Text(
                text = "Room ${room.roomNo}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Status: ${room.status}"
            )
    }
}
@Preview
@Composable
fun previewCardComposable(){
    cardComposable(room = RoomModel())
}
