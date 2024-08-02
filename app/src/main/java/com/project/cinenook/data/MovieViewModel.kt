package com.project.cinenook.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel(), MovieListViewModel {
    override val moviePager = Pager(PagingConfig(pageSize = 10)) {
        MovieDataSource(repository)
    }.flow.cachedIn(viewModelScope)
}