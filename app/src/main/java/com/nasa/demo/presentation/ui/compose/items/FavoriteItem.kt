package com.nasa.demo.presentation.ui.compose.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nasa.demo.R
import com.nasa.demo.domain.model.NasaImageUIItem

@Composable
fun FavoriteItem(item: NasaImageUIItem) {
    val painter = loadPicture(
        url = item.imageUrl, placeholder = painterResource(id = R.drawable.ic_placeholder)
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onBackground) // Placeholder background
    )
}