package com.assignment2.movieApp.view
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.assignment2.movieApp.databinding.ActivityMovieDetailsBinding
import com.assignment2.movieApp.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: MovieViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        // Get Movie ID from Intent and fetch details
        val movieId = intent.getStringExtra("MOVIE_ID")
        movieId?.let { id ->
            viewModel.getMovieDetails(id)
        }

        // Observe LiveData for movie details
        viewModel.movieDetails.observe(this) { movie ->
            // Update UI with movie details if movie is not null
            movie?.let {
                binding.apply {
                    tvMovieTitle.text = "Title: ${it.title}"
                    tvMovieYear.text = "Year: ${it.year}"
                    tvMovieRating.text = "Age Restriction: ${it.rated}"
                    tvMovieDescription.text = "Description: ${it.plot}"
                    tvMovieImdbRating.text = "IMDB Rating: ${it.imdbRating}"
                    tvMovieStudio.text = "Studio: ${it.studio}"

                    Glide.with(this@MovieDetailsActivity)
                        .load(it.poster)
                        .into(ivMoviePoster)
                }
            }
        }

        // Setup back button
        binding.btnGoBack.setOnClickListener { finish() }
    }
}