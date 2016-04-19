package com.udacitynanodegree.trino.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.udacitynanodegree.trino.popularmovies.Adapters.MovieAdapter;
import com.udacitynanodegree.trino.popularmovies.Classes.Movie;
import com.udacitynanodegree.trino.popularmovies.Classes.MovieResult;
import com.udacitynanodegree.trino.popularmovies.MoviesAPI.MoviesApiInterface;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
public class MainActivity extends AppCompatActivity {
    private TextView textViewError;
    private RecyclerView listPopularMovies;
    private MovieAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Movie> movies;
    private Retrofit client;
    private String baseUrl = "http://api.themoviedb.org";
    private boolean networkError = false;
    private static final String API_KEY = "e5807842fb9d38020733ffe1092349a1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewError = (TextView)findViewById(R.id.textNetworkError);
        setSupportActionBar(toolbar);
        listPopularMovies = (RecyclerView)findViewById(R.id.listPopularMovies);
        setUpOrientation();
        adapter =new MovieAdapter(getApplicationContext(), new MovieAdapter.MovieClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = adapter.getMovie(position);
                if(movie!=null)
                    startActivity(new Intent(getApplicationContext(),DetailsActivity.class).putExtra("Movie",movie));
            }
        });
        if(savedInstanceState == null) {
            fetchMovies();
        }else{
            movies = savedInstanceState.getParcelableArrayList("movies");
            restoreList();
        }
        listPopularMovies.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(!networkError){
            outState.putParcelableArrayList("movies", movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        movies = state.getParcelableArrayList("movies");
        restoreList();
    }

    private void displayMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpOrientation();
    }

    private void fetchMovies(){
        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });
        client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoviesApiInterface api = client.create(MoviesApiInterface.class);
        Call<MovieResult> call = api.getPopularMovies("popularity.desc",API_KEY);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(retrofit.Response<MovieResult> response, Retrofit retrofit1) {
                if (response.isSuccess()) {
                    textViewError.setVisibility(View.GONE);
                    textViewError.setText(null);
                    movies = response.body().getResults();
                    networkError = false;
                    adapter.setMoviesList(movies);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText("Ocurrió un error al obtener la información");
                networkError = true;
            }
        });
    }

    private void setUpOrientation(){
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int spanSize = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                spanSize = 2;
                break;
            case Surface.ROTATION_90:
                spanSize = 3;
                break;
            case Surface.ROTATION_180:
                spanSize = 2;
                break;
            case Surface.ROTATION_270:
                spanSize = 3;
                break;
            default:
                spanSize = 2;
                break;
        }
        mLayoutManager = new GridLayoutManager(getApplicationContext(), spanSize);
        listPopularMovies.setLayoutManager(mLayoutManager);
    }

    private void restoreList(){
        if(!networkError){
            adapter.setMoviesList(movies);
        }
    }
}
