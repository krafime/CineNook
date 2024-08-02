package com.project.cinenook.data

import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): MoviePopularResponse {
        return movieApi.getPopularMovies(page)
    }

    override suspend fun getMovieDetail(id: Int): MovieDetailResponse {
        return movieApi.getMovieDetail(id)
    }

    override suspend fun getRecommendations(id: Int): MovieRecommendationResponse {
        return movieApi.getRecommendations(id)
    }

    override suspend fun searchMovie(query: String, page: Int): MoviePopularResponse {
        return movieApi.searchMovie(query, page)
    }
}