package com.project.cinenook.data

import com.project.cinenook.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MovieApi {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MoviePopularResponse

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

        operator fun invoke(): MovieApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getRetrofitClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApi::class.java)
        }

        private fun getRetrofitClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer ${BuildConfig.tokenApi}")
                            .build()
                    )
                }
                .build()
        }
    }
}