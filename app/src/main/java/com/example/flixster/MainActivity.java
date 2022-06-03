package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import util.ItemClickSupport;

public class MainActivity extends AppCompatActivity {
    public static final String URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=7ac39a56c7c85383e4619ae5b5a43999";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();

        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                Log.d(TAG, "Success");
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, results.toString());
                    movies.addAll(Movie.fromJSONArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, movies.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failure");
            }
        });


        ItemClickSupport.addTo(rvMovies).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                    i.putExtra(Movie.class.getSimpleName(),
                            Parcels.wrap(movies.get(position)));
                    startActivity(i);
                }
        );
    }
}