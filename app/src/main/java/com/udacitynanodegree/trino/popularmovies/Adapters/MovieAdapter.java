package com.udacitynanodegree.trino.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacitynanodegree.trino.popularmovies.Classes.Movie;
import com.udacitynanodegree.trino.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trino on 27/10/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolderMovie> {
    private static String urlImages = "http://image.tmdb.org/t/p/w150/";
    private List<Movie> movies = new ArrayList<Movie>();
    private LayoutInflater layoutInflater;
    private Context context;
    private MovieClickListener listener;
    public MovieAdapter(Context context, MovieClickListener listener){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    public void setMoviesList(List<Movie> movies){
        this.movies = movies;
        notifyItemRangeChanged(0,this.movies.size());
    }

    @Override
    public ViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType){
        View view = layoutInflater.inflate(R.layout.row_item_movie,parent,false);
        final ViewHolderMovie viewHolderMovie = new ViewHolderMovie(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,viewHolderMovie.getAdapterPosition());
            }
        });
        return viewHolderMovie;
    }

    public Movie getMovie(int position){
        return movies.get(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMovie holder, int position){
        Movie currentMovie = movies.get(position);
        loadThumbnail(currentMovie.getPosterPath(),holder);
    }

    private void loadThumbnail(String urlThumbnail, final ViewHolderMovie holder){
        Picasso.with(context)
                .load(urlImages + urlThumbnail)
                .into(holder.movieThumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    static class ViewHolderMovie extends RecyclerView.ViewHolder{
        private ImageView movieThumbnail;
        private TextView movieTitle;
        private TextView movieReleaseDate;
        private RatingBar movieAudienceScore;
        public ViewHolderMovie(View itemView){
            super(itemView);
            movieThumbnail = (ImageView)itemView.findViewById(R.id.movieThumbnail);
        }
    }

    public interface MovieClickListener{
        public void onItemClick(View view, int position);
    }
}
