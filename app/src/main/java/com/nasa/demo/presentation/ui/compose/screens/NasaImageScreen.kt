package com.nasa.demo.presentation.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nasa.demo.presentation.ui.compose.items.FavoriteItem
import com.nasa.demo.presentation.ui.compose.items.NasaImageItemView
import com.nasa.demo.presentation.ui.compose.items.SearchBar
import com.nasa.demo.presentation.ui.compose.items.ShowFavoriteDialog
import com.nasa.demo.presentation.viewmodel.NasaImageViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NasaImageScreen(
    viewModel: NasaImageViewModel
) {

    val isRefreshing by viewModel.isLoading.observeAsState(initial = false)
    val exceptionMessage by viewModel.exceptionMessage.observeAsState(initial = "")
    val lazyPagingItems = viewModel.images.collectAsLazyPagingItems()
    val favoriteImages by viewModel.favoriteImages.observeAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nasa Images") },
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (exceptionMessage.isNotEmpty()) {
                Text(
                    text = exceptionMessage,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = { viewModel.refresh() } // Call refresh on pull
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                Column(modifier = Modifier.fillMaxSize()) {

                    SearchBar(
                        query = query,
                        onQueryChange = {
                            query = it
                            viewModel.updateQuery(query)
                        }
                    )

                    if (favoriteImages.isNotEmpty()) {
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(8.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(favoriteImages) { item ->
                                FavoriteItem(item)
                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            val item = lazyPagingItems[index]
                            item?.let {
                                NasaImageItemView(item, onItemClick = { clickedItem ->
                                    viewModel.toggleFavorite(clickedItem)
                                })
                            }
                        }

                        when {
                            lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                                item(span = { GridItemSpan(3) }) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(16.dp)
                                    )
                                }
                            }

                            lazyPagingItems.loadState.append is LoadState.Loading -> {
                                item(span = { GridItemSpan(3) }) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}