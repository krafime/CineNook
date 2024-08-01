package com.project.cinenook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieRecommendationViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel(){
    private val _recommendation = MutableLiveData<MovieRecommendationResponse>()
    val recommendation: LiveData<MovieRecommendationResponse> get() = _recommendation

    suspend fun fetchRecommendation(id: Int){
        _recommendation.value = repository.getRecommendations(id)
    }

    fun clearState(){
        _recommendation.value = null
    }
}