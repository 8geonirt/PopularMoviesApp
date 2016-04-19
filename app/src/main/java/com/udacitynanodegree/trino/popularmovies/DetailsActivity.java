package com.udacitynanodegree.trino.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacitynanodegree.trino.popularmovies.Classes.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    private static String urlImages = "http://image.tmdb.org/t/p/w500/";
    private TextView movieTitle,sinopsis, releaseDate, userRating;
    private ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Movie movie = (Movie)getIntent().getParcelableExtra("Movie");
        movieTitle = (TextView)findViewById(R.id.txtTitle);
        sinopsis = (TextView)findViewById(R.id.txtSinopsis);
        releaseDate = (TextView)findViewById(R.id.txtReleaseDate);
        userRating = (TextView)findViewById(R.id.txtUserRating);
        movieTitle.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate().split("-")[0]);
        userRating.setText(movie.getVoteAverage().toString()+"/10");
        poster = (ImageView)findViewById(R.id.poster);
        Picasso.with(getApplicationContext())
                .load(urlImages + movie.getPosterPath())
                .into(poster);
        sinopsis.setText(movie.getOverview());
    }

}
