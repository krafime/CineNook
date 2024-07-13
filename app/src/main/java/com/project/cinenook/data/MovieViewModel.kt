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
) : ViewModel() {
    val moviePager = Pager(PagingConfig(pageSize = 20)) {
        MovieDataSource(repository)
    }.flow.cachedIn(viewModelScope)
}