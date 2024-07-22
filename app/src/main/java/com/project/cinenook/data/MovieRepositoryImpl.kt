package com.project.cinenook.data

class MovieRepositoryImpl(
    private val movieApi: MovieApi
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): MoviePopularResponse {
        return movieApi.getPopularMovies(page)
    }

    override suspend fun getMovieDetail(id: Int): MovieDetailResponse {
        return movieApi.getMovieDetail(id)
    }
}