package com.project.cinenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.cinenook.data.MovieDetailViewModel
import com.project.cinenook.data.MovieRecommendationViewModel
import com.project.cinenook.data.MovieViewModel
import com.project.cinenook.data.SearchMovieViewModel
import com.project.cinenook.ui.theme.CineNookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val movieRecommendationViewModel: MovieRecommendationViewModel by viewModels()
    private val searchMovieViewModel: SearchMovieViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
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
                    val scrollBehavior = rememberTopAppBarScrollBehavior()
                    var showSearch by remember { mutableStateOf(false) }
                    var searchQuery by remember { mutableStateOf("") }

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    if (showSearch) {
                                        OutlinedTextField(
                                            value = searchQuery,
                                            onValueChange = { newQuery ->
                                                searchQuery = newQuery
                                            },
                                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                            placeholder = { Text(text = "Search movies...", fontSize = 14.sp) },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                            keyboardActions = KeyboardActions(onSearch = {
                                                searchMovieViewModel.onQueryChange(searchQuery)
                                                navController.navigate("search") {
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }),
                                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 14.sp
                                            )
                                        )
                                    } else {
                                        Text(text = "CineNook")
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        showSearch = !showSearch
                                        if (!showSearch) {
                                            searchQuery = ""
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if (showSearch) Icons.TwoTone.Close else Icons.TwoTone.Search,
                                            contentDescription = if (showSearch) "Close search" else "Search"
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehavior
                            )
                        }
                    ) { paddingValues ->
                        NavHost(
                            modifier = Modifier
                                .padding(paddingValues)
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            navController = navController,
                            startDestination = "movie_list"
                        ) {
                            composable("movie_list") {
                                MovieList(viewModel = movieViewModel, navController = navController)
                            }
                            composable(
                                "movie_detail/{movieId}",
                                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                                MovieDetail(
                                    movieId = movieId,
                                    movieRecommend = movieRecommendationViewModel,
                                    viewModel = movieDetailViewModel,
                                    navController = navController,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("search") {
                                MovieList(
                                    viewModel = searchMovieViewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTopAppBarScrollBehavior(): TopAppBarScrollBehavior {
    return TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
}