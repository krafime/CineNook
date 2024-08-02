package com.project.cinenook.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel(), MovieListViewModel {
    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    override val moviePager = _query.flatMapLatest { query ->
        Pager(PagingConfig(pageSize = 10)) {
            SearchMovieDataSource(repository, query)
        }.flow.cachedIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}