package com.project.cinenook.data

//@Suppress("UNCHECKED_CAST")
//class ViewModelFactory(
//    private val movieRepository: MovieRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
//                MovieViewModel(movieRepository) as T
//            }
//            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
//                MovieDetailViewModel(movieRepository) as T
//            }
//            modelClass.isAssignableFrom(MovieRecommendationViewModel::class.java) -> {
//                MovieRecommendationViewModel(movieRepository) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}
