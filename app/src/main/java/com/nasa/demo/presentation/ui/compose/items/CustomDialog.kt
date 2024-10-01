package com.nasa.demo.presentation.ui.compose.items

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.nasa.demo.domain.model.NasaImageUIItem

@Composable
fun ShowFavoriteDialog(
    item: NasaImageUIItem,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val action = if (item.isFavorite) "remove from" else "add to"
    val message = "Are you sure you want to $action your favorites?"

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Confirm Action") },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No")
            }
        }
    )
}
