package com.project.cinenook

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.project.cinenook.data.ResultsItem

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
                text = movie.title ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(6.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
    )
    MovieCard(movie = movie, onClick = {})
}