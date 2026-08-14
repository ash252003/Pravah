package com.example.pravah.view.Staff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pravah.Components.cardComposable
import com.example.pravah.model.RoomModel
import com.example.pravah.ui.theme.PravahAppTheme

@Composable
fun RoomList(rooms: List<RoomModel>){
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(
                items = rooms,
                key={it.id},
            ){
                room->
                cardComposable(room=room)
            }
        }
    }
}
@Preview
@Composable
fun PreviewRoomList(){
    PravahAppTheme {
        RoomList(
            rooms = listOf(
                RoomModel(
                    id = "1",
                    institutionId = "ABC123",
                    roomNo = "701",
                    status = "active"
                ),
                RoomModel(
                    id = "2",
                    institutionId = "ABC123",
                    roomNo = "702",
                    status = "active"
                ),
                RoomModel(
                    id = "3",
                    institutionId = "ABC123",
                    roomNo = "703",
                    status = "inactive"
                )
            )
        )
    }
}