package com.udacitynanodegree.trino.popularmovies.MoviesAPI;

import com.udacitynanodegree.trino.popularmovies.Classes.Movie;
import com.udacitynanodegree.trino.popularmovies.Classes.MovieResult;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Trino on 27/10/2015.
 */
public interface MoviesApiInterface {
    @GET("/3/discover/movie")
    Call<MovieResult> getPopularMovies(@Query("sort_by") String sortBy,@Query("api_key") String apiKey);
}
