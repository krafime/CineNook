package com.project.cinenook

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.cinenook.data.MovieViewModel

@Composable
fun MovieList(viewModel: MovieViewModel, navController: NavController) {
    val movieList = viewModel.moviePager.collectAsLazyPagingItems()

    // Observing load state
    val loadState = movieList.loadState

    Box(modifier = Modifier.fillMaxSize()) {
        when (val refreshState = loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is LoadState.Error -> {
                Text(
                    text = "Error loading data: ${refreshState.error.message}",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            is LoadState.NotLoading -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(movieList.itemCount) { index ->
                        val movie = movieList[index]
                        if (movie != null) {
                            MovieCard(movie = movie) { movieId ->
                                navController.navigate("movie_detail/$movieId")
                            }
                        }
                    }
                }
            }
        }

        if (loadState.append == LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}