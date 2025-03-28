package com.yourusername.moviesearchapp.model

import com.assignment2.movieApp.model.Movie


data class MovieSearchResponse(
    val Search: List<Movie>? = null,
    val totalResults: String? = null,
    val Response: String? = null
)