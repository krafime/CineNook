package com.project.cinenook.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MoviePopularResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int
    ): MovieDetailResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(
        @Path("movie_id") id: Int
    ): MovieRecommendationResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviePopularResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}