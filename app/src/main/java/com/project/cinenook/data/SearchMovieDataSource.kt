package com.project.cinenook.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class SearchMovieDataSource(
    private val repository: MovieRepository,
    private val query: String
) : PagingSource<Int, ResultsItem>(){
    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = repository.searchMovie(query, nextPageNumber)
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (response.results.isNotEmpty()) nextPageNumber + 1 else null
            )
        } catch (e: IOException) {
            Log.e("SearchMovieDataSource", "Network error: ${e.message}", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("SearchMovieDataSource", "HTTP error: ${e.message}", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("SearchMovieDataSource", "Unexpected error: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}