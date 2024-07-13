package com.project.cinenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.project.cinenook.data.MovieApi
import com.project.cinenook.data.MovieRepositoryImpl
import com.project.cinenook.data.MovieViewModel
import com.project.cinenook.data.ResultsItem
import com.project.cinenook.ui.theme.CineNookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Suppress("UNCHECKED_CAST")
    private val viewModel by viewModels<MovieViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(MovieRepositoryImpl(MovieApi())) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineNookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieList(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MovieList(viewModel: MovieViewModel) {

    val movieList = viewModel.moviePager.collectAsLazyPagingItems()

    LazyColumn {
        items(movieList.itemCount) { index ->
            val movie = movieList[index]
            if (movie != null) {
                MovieCard(movie = movie)
            }
        }
    }
}

@Composable
fun MovieCard(movie: ResultsItem) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val showShimmerBackdrop = remember { mutableStateOf(true) }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w185/${movie.backdropPath}")
                    .transformations(
                        RoundedCornersTransformation(16f),
                        BlurTransformation(LocalContext.current, 25f, 1f)
                    )
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .background(
                        shimmerBrush(
                            targetValue = 1300f,
                            showShimmer = showShimmerBackdrop.value
                        )
                    ),
                onSuccess = { showShimmerBackdrop.value = false },
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val showShimmer = remember { mutableStateOf(true) }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w185/${movie.posterPath}")
                        .transformations(RoundedCornersTransformation(16f))
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                        .width(100.dp)
                        .height(150.dp)
                        .align(Alignment.CenterVertically),
                    onSuccess = { showShimmer.value = false },
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = movie.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 6.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                    )
                    Text(
                        text = movie.overview ?: "",
                        modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MyMovieCardPreview(){
//    val movie = ResultsItem(
//        id = 1022789,
//        title = "Inside Out 2 uwhdd udhuh wduhidw iwdi ",
//        posterPath = "https://image.tmdb.org/t/p/w185/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//        overview = "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone.",
//        releaseDate = "2024-06-11",
//        voteAverage = 7.5,
//        popularity = 10032.488,
//    )
//    MovieCard(movie = movie)
//}