package com.project.cinenook.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieListViewModel {
    val moviePager: Flow<PagingData<ResultsItem>>
}