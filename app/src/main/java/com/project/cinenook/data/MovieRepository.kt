package com.project.cinenook.data

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): MoviePopularResponse
    suspend fun getMovieDetail(id: Int): MovieDetailResponse
}