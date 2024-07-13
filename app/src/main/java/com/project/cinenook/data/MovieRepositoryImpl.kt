package com.project.cinenook.data

class MovieRepositoryImpl(
    private val movieApi: MovieApi
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): MoviePopularResponse {
        return movieApi.getPopularMovies(page)
    }
}