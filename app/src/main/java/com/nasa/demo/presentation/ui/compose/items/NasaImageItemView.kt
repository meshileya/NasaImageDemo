package com.nasa.demo.presentation.ui.compose.items

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nasa.demo.domain.model.NasaImageUIItem
import com.nasa.demo.R

@Composable
fun NasaImageItemView(
    item: NasaImageUIItem, onItemClick: (NasaImageUIItem) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val isFavoriteIcon =
        if (item.isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24

    // Container for the entire card
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { showDialog = true },
//        .background(MaterialTheme.colors.onBackground),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium

    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            val painter = loadPicture(
                url = item.imageUrl, placeholder = painterResource(id = R.drawable.ic_placeholder)
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            ) {


                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onBackground) // Placeholder background
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp),
//                        .padding(4.dp),
                    painter = painterResource(id = isFavoriteIcon),
                    contentDescription = "Favorite"
                )

            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    if (showDialog) {
        ShowFavoriteDialog(
            item = item,
            onConfirm = {
                showDialog = false
                onItemClick(item.copy(isFavorite = !item.isFavorite))
            },
            onDismiss = { showDialog = false }
        )
    }

}

@Composable
fun loadPicture(url: String, placeholder: Painter): Painter {

    var state by remember { mutableStateOf(placeholder) }

    val context = LocalContext.current
    val result = object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(p: Drawable?) {
            state = placeholder
        }

        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?,
        ) {
            state = BitmapPainter(resource.asImageBitmap())
        }
    }
    try {
        Glide.with(context).asBitmap().load(url).into(result)
    } catch (_: Exception) {
    }
    return state
}
