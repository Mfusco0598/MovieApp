package com.assignment2.movieApp.model

data class Movie(
    val title: String,
    val year: String,
    val imdbID: String,
    val imdbRating: String?,
    val type: String,
    val studio: String?,
    val poster: String,
    val plot: String? = "null",
    val rated: String? = "null"
)