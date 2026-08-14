package com.example.pravah.view.Staff

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

@Composable
fun RoomList(rooms: List<RoomModel>){
    Surface(modifier = Modifier.padding()) {
        LazyColumn(modifier = Modifier.padding(20.dp)) {
            items(
                items = rooms,
                key={it.id}
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
    RoomList(
        rooms = listOf(
            RoomModel(
                id = "1",
                institutionId = "ABC123",
                roomNo = "701",
                status = "working"
            ),
            RoomModel(
                id = "2",
                institutionId = "ABC123",
                roomNo = "702",
                status = "working"
            ),
            RoomModel(
                id = "3",
                institutionId = "ABC123",
                roomNo = "703",
                status = "damaged"
            )
        )
    )
}