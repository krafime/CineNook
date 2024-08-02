package com.project.cinenook

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.project.cinenook.data.MovieDetailViewModel
import com.project.cinenook.data.MovieRecommendationViewModel
import com.project.cinenook.data.RecommendationResultsItem


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetail(
    movieId: Int,
    movieRecommend: MovieRecommendationViewModel,
    viewModel: MovieDetailViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    val movieDetail by viewModel.movieDetail.observeAsState()
    val showShimmerBackdrop = remember { mutableStateOf(true) }
    val showShimmer = remember { mutableStateOf(true) }

    // Ensure this LaunchedEffect runs whenever movieId changes
    LaunchedEffect(movieId) {
        showShimmerBackdrop.value = true
        showShimmer.value = true
        viewModel.fetchMovieDetail(movieId)
        movieRecommend.fetchRecommendation(movieId)
    }

    // Menangani event tombol kembali
    BackHandler {
        viewModel.clearState()
        movieRecommend.clearState()
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        movieDetail?.let { movie ->
            // Backdrop image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w780/${movie.backdropPath}")
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = "Backdrop",
                modifier = Modifier
                    .background(shimmerBrush(showShimmerBackdrop.value))
                    .fillMaxWidth()
                    .height(300.dp),
                error = painterResource(id = R.drawable.no_image),
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            // add arrow back
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp, top = 32.dp)
                    .clickable {
                        viewModel.clearState()
                        movieRecommend.clearState()
                        onBack()
                    }
            )

            // Content over the backdrop
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(150.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Poster image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://image.tmdb.org/t/p/w185/${movie.posterPath}")
                                .transformations(RoundedCornersTransformation(16f))
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Poster",
                            modifier = Modifier
                                .background(
                                    shimmerBrush(showShimmer.value)
                                )
                                .width(100.dp)
                                .height(150.dp),
                            onSuccess = { showShimmer.value = false },
                            onError = { showShimmer.value = false },
                            error = painterResource(id = R.drawable.no_image),
                            contentScale = ContentScale.Crop,
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Movie details
                        Column {
                            Text(
                                text = movie.title ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${(movie.voteAverage ?: 0.0).toString().take(3)}/10", // Format rating
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                                Text(text = " (${movie.voteCount ?: 0})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Genres
                            movie.genres?.let { genres ->
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),  // Horizontal spacing between items
                                    verticalArrangement = Arrangement.spacedBy(4.dp)  // Vertical spacing between rows
                                ) {
                                    genres.forEach { genre ->
                                        genre?.name?.let { genreName ->
                                            Box(
                                                modifier = Modifier
                                                    .border(
                                                        1.dp,
                                                        MaterialTheme.colorScheme.primary,
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .background(
                                                        MaterialTheme.colorScheme.inversePrimary.copy(
                                                            alpha = 0.6f
                                                        ), RoundedCornerShape(4.dp)
                                                    ) // Add background with alpha
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = genreName,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onBackground // Ensure text is readable
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tagline
                if (movie.tagline.isNullOrEmpty().not()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = movie.tagline ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Movie overview
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = movie.overview ?: "No description available",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // First Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        movie.runtime?.let { runtime ->
                            Column(
                             modifier = Modifier.weight(1f)
                            ){
                                Text(
                                    text = "Duration",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = (runtime.takeIf { it > 0 }?.let { "${it / 60}h ${it % 60}m" } ?: "N/A"),
                                    style = MaterialTheme.typography.bodySmall)
                            }

                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        movie.releaseDate?.let { releaseDate ->
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Release Date",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = Utils.formatReleaseDate(releaseDate),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second Row

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Language origin
                            Text(
                                text = "Original Language",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                text = movie.spokenLanguages?.firstOrNull()?.englishName ?: "N/A",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Popularity
                            Text(
                                text = "Popularity",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                text = movie.popularity?.toString() ?: "N/A",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Menampilkan daftar rekomendasi film horizontal
                val movieRecommendation by movieRecommend.recommendation.observeAsState()

                movieRecommendation?.results?.let { results ->
                    if (results.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Related movies
                        Text(
                            text = "Related Movies",
                            style = MaterialTheme.typography.titleSmall
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(results.size) { movieItem ->
                                key(movieItem) {  // Add key for proper recomposition
                                    results[movieItem]?.let {
                                        MovieItem(movie = it) { selectedMovieId ->
                                            navController.navigate("movie_detail/$selectedMovieId") {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: RecommendationResultsItem, onClick: (Int) -> Unit) {

    val showShimmer = remember { mutableStateOf(true) }

    // Reset shimmer state when movie changes
    LaunchedEffect(movie.id) {
        showShimmer.value = true
    }

    Card(
        modifier = Modifier
            .padding(6.dp)
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable { onClick(movie.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w154/${movie.posterPath}")
                    .transformations(RoundedCornersTransformation(16f))
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        shimmerBrush(
                            showShimmer = showShimmer.value
                        )
                    )
                    .width(100.dp)
                    .height(150.dp),
                onSuccess = { showShimmer.value = false },
                onError = { showShimmer.value = false },
                error = painterResource(id = R.drawable.no_image),
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(6.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
