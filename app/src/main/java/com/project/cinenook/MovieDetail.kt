package com.project.cinenook

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.project.cinenook.data.MovieDetailViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetail(
    movieId: Int,
    viewModel: MovieDetailViewModel
) {
    val movieDetail by viewModel.movieDetail.observeAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetail(movieId)
    }

    movieDetail?.let { movie ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Backdrop image
            val showShimmerBackdrop = remember { mutableStateOf(true) }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w780/${movie.backdropPath}")
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = "Backdrop",
                modifier = Modifier
                    .background(shimmerBrush(
                        showShimmer = showShimmerBackdrop.value
                    ))
                    .fillMaxWidth()
                    .height(300.dp),
                onSuccess = { showShimmerBackdrop.value = false },
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
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
                    val showShimmer = remember { mutableStateOf(true) }
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
                                    shimmerBrush(
                                        showShimmer = showShimmer.value
                                    )
                                )
                                .width(100.dp)
                                .height(150.dp),
                            onSuccess = { showShimmer.value = false },
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
                                    text = (movie.voteAverage ?: 0.0).toString().take(3), // Format rating
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Release Date: ${movie.releaseDate ?: "N/A"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )

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
                                                            alpha = 0.7f
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
                movie.tagline?.let { tagline ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = tagline,
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
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    text = movie.overview ?: "No description available",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

