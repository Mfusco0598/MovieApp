package com.assignment2.movieApp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.assignment2.movieApp.model.Movie
import com.assignment2.movieApp.network.MovieApiClient

class MovieViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movieDetailsLiveData = MutableLiveData<Movie>()

    val movies: LiveData<List<Movie>> get() = _movies

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?> get() = _movieDetails


    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MovieApiClient.searchMovies(query)
                Log.d("SearchMovies", "API Response: $response")
                response?.let {
                    val jsonObject = JSONObject(it)
                    val error = jsonObject.optString("Error")
                    if (error.isNotEmpty()) {
                        return@launch
                    }
                    val jsonArray = jsonObject.optJSONArray("Search")
                    val movieList = mutableListOf<Movie>()

                    if (jsonArray != null) {
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            movieList.add(
                                Movie(
                                    title = item.optString("Title"),
                                    year = item.optString("Year"),
                                    imdbID = item.optString("imdbID"),
                                    type = item.optString("Type"),
                                    poster = item.optString("Poster"),
                                    rated = item.optString("Rated"),
                                    imdbRating = item.optString("imdbRating").takeIf { it.isNotEmpty() } ?: "N/A",
                                    studio = item.optString("Production").takeIf { it.isNotEmpty() } ?: "Unknown"
                                )
                            )
                        }
                    }

                    _movies.postValue(movieList)
                }
            } catch (e: Exception) {
                Log.e("SearchMovies", "Error parsing movies: ${e.message}")
            }
        }
    }

    fun getMovieDetails(imdbId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = MovieApiClient.getMovieDetails(imdbId)
            response?.let {
                val jsonObject = JSONObject(it)
                val movie = Movie(
                    title = jsonObject.optString("Title"),
                    year = jsonObject.optString("Year"),
                    imdbID = jsonObject.optString("imdbID"),
                    type = jsonObject.optString("Type"),
                    poster = jsonObject.optString("Poster"),
                    plot = jsonObject.optString("Plot"),
                    rated = jsonObject.optString("Rated"),
                    imdbRating = jsonObject.optString("imdbRating").takeIf { it.isNotEmpty() } ?: "N/A",
                    studio = jsonObject.optString("Production").takeIf { it.isNotEmpty() } ?: "Unknown"
                )
                _movieDetails.postValue(movie)
            }
        }
    }
}

