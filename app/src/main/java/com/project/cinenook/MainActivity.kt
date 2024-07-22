package com.project.cinenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.project.cinenook.data.MovieApi
import com.project.cinenook.data.MovieDetailViewModel
import com.project.cinenook.data.MovieRepository
import com.project.cinenook.data.MovieRepositoryImpl
import com.project.cinenook.data.MovieViewModel
import com.project.cinenook.data.ResultsItem
import com.project.cinenook.data.ViewModelFactory
import com.project.cinenook.ui.theme.CineNookTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: MovieRepository

    private val movieViewModel: MovieViewModel by viewModels {
        ViewModelFactory(repository)
    }
    private val movieDetailViewModel: MovieDetailViewModel by viewModels {
        ViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineNookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    repository = MovieRepositoryImpl(MovieApi.invoke())
                    NavHost(navController = navController, startDestination = "movie_list") {
                        composable("movie_list") {
                            MovieList(viewModel = movieViewModel, navController = navController)
                        }
                        composable("movie_detail/{movieId}") { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 0
                            MovieDetail(movieId, movieDetailViewModel)
                        }
                    }
                }
            }
        }
    }
}

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


@Composable
fun MovieCard(movie: ResultsItem, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable { onClick(movie.id ?: 0) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val showShimmer = remember { mutableStateOf(true) }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w92/${movie.posterPath}")
                    .transformations(RoundedCornersTransformation(16f))
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .background(
                        shimmerBrush(
                            showShimmer = showShimmer.value
                        )
                    )
                    .width(100.dp)
                    .height(150.dp)
                    .padding(8.dp),
                onSuccess = { showShimmer.value = false },
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(6.dp),
            )
            Text(
                text = movie.releaseDate ?: "",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyMovieCardPreview(){
    val movie = ResultsItem(
        id = 1022789,
        title = "Inside Out 2",
        posterPath = "https://image.tmdb.org/t/p/w185/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
        overview = "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone.",
        releaseDate = "2024-06-11",
        voteAverage = 7.5,
        popularity = 10032.488,
    )
    MovieCard(movie = movie, onClick = {})
}