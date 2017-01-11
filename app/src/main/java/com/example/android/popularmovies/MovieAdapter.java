package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link MovieAdapter} exposes a grid of movie posters to a
 * {@link android.support.v7.widget.RecyclerView}
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    // An onClick handler that we've defined to make it easy for an Activity to interface with our
    // RecyclerView.
    private final MovieAdapterOnClickHandler mClickHandler;

    private List<Movie> mMovieData;

    private Context mContext;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler, List<Movie> movieData) {
        mClickHandler = clickHandler;
        mMovieData = movieData;
        mContext = context;
    }

    // The interface that receives onClick messages.
    public interface MovieAdapterOnClickHandler {
        void onClick(String moviePoster);
    }

    public void updateMovies(ArrayList<Movie> moviesList) {
        this.mMovieData = moviesList;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView is
     * laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item you can use this viewType
     *                 integer to provide a different layout.
     * @return A new MovieAdapterViewHolder that holds the View for each list item.
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Gets the context from the viewGroup and stores in variable
        Context context = viewGroup.getContext();
        // Gets the layout id
        int layoutIdForListItem = R.layout.movie_list_item;
        // Creates the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        // inflates the View
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        // Returns the new ViewHolder
        return new MovieAdapterViewHolder(view);

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
     * In this method, we update the contents of the ViewHolder to display a poster of a movie for
     * this particular position, using the "position" argument that is conveniently passed into us.
     * @param holder The ViewHolder which should be updated to represent the contents of the item
     *               at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMovieData.get(position);

        // Set the imageview to the movie data
        ImageView imageView = holder.mMovieImageView;
        String posterPath = movie.getmPosterPath();
        Picasso.with(mContext).load(NetworkUtils.BASE_URL + posterPath).into(imageView);
    }


    /**
     * This method returns the number of items to display. It is used behind the scenes to help
     * layout our Views and for animations.
     *
     * @return  The number of items available in our query.
     */
    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    // Cache of the children views for a movie list item
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        // This gets called when a child view is clicked.
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }

}
