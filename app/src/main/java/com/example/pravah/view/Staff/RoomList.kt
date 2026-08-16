package com.example.pravah.view.Staff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pravah.Components.cardComposable
import com.example.pravah.model.RoomModel
import com.example.pravah.ui.theme.PravahAppTheme
import com.example.pravah.viewmodel.ClassViewModel


@Composable
fun RoomListScreen(
    navController: NavController,
    viewModel: ClassViewModel
) {

    RoomList(
        rooms = viewModel.rooms,
        isLoading = viewModel.isLoading,

        onRoomClick = { room ->

            navController.navigate(
                "roomInfo/${room.id}"
            )
        }
    )
}

@Composable
fun RoomList(
    rooms: List<RoomModel>,
    isLoading: Boolean = false,
    onRoomClick: (RoomModel) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        when {

            isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            rooms.isEmpty() -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No rooms yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 12.dp
                    ),
                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    items(
                        items = rooms,
                        key = { it.id }
                    ) { room ->

                        cardComposable(
                            room = room,
                            onClick = {
                                onRoomClick(room)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoomList() {
    PravahAppTheme {
        RoomList(
            rooms = listOf(
                RoomModel(id = "1", institutionId = "ABC123", roomNo = "701", devices = emptyList(), status = "active"),
                RoomModel(id = "2", institutionId = "ABC123", roomNo = "702", devices = emptyList(), status = "active"),
                RoomModel(id = "3", institutionId = "ABC123", roomNo = "703", devices = emptyList(), status = "inactive"),
            ),
            onRoomClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoomListLoading() {
    PravahAppTheme {
        RoomList(rooms = emptyList(), isLoading = true, onRoomClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoomListEmpty() {
    PravahAppTheme {
        RoomList(rooms = emptyList(), isLoading = false, onRoomClick = {})
    }
}
