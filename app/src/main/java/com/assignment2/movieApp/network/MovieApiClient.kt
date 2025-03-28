package com.assignment2.movieApp.network

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

object MovieApiClient {
    private const val BASE_URL = "https://www.omdbapi.com/"
    private const val API_KEY = "7cf89d3d"

    fun searchMovies(query: String): String? {
        val urlString = "$BASE_URL?s=$query&apikey=$API_KEY&i="
        return makeRequest(urlString)
    }

    fun getMovieDetails(imdbId: String): String? {
        val urlString = "$BASE_URL?i=$imdbId&apikey=$API_KEY"
        return makeRequest(urlString)
    }

    private fun makeRequest(urlString: String): String? {
        return try {
            Log.d("MovieApiClient", "Requesting URL: $urlString")
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = reader.readText()
            reader.close()
            Log.d("MovieApiClient", "API Response: $response")
            response
        } catch (e: Exception) {
            Log.e("MovieApiClient", "API Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}