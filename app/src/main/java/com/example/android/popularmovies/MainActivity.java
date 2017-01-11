package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MovieAdapter
        .MovieAdapterOnClickHandler {

    public static final String PREFS_NAME = "MyPrefsFile";

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private boolean sortBy;
    private List<Movie> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references using findViewById for the various variables.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Restore preferences TRUE = sort by popularity, FALSE = sort by top rated
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        sortBy = settings.getBoolean("sortPreference", true);



        // Set up the GridLayoutManager
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Kick off the asynctask
        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();
        new FetchMoviesTask().execute();
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks
     *
     * @param movieIndex The movie that was clicked.
     */
    @Override
    public void onClick(String movieIndex) {
        int index = Integer.valueOf(movieIndex);
        Movie movieClicked = moviesList.get(index);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("title", movieClicked.getmTitle());
        intent.putExtra("plot", movieClicked.getmPlotSynopsis());
        intent.putExtra("releaseDate", movieClicked.getmReleaseDate());
        intent.putExtra("posterPath", movieClicked.getmPosterPath());
        intent.putExtra("voteAverage", movieClicked.getmVoteAverage());
        startActivity(intent);
    }

    // Creates the options menu, uses mainactivitymenu.xml for the menu items.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivitymenu, menu);
        return true;
    }

    // Sets what happens when options menu items are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        switch (id) {
            case R.id.action_sort_most_popular:
                sortBy = settings.getBoolean("sortPreference", true);
                loadMovieData();
                mMovieAdapter.notifyDataSetChanged();
                break;
            case R.id.action_sort_highest_rated:
                sortBy = settings.getBoolean("sortPreference", false);
                loadMovieData();
                mMovieAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // public class that extends AsyncTask to load things in background.
    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            // if sortBy is true, use URL for sorted by popularity, if false use top rated
            return NetworkUtils.fetchMoviesData(sortBy);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> strings) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (strings != null) {
                moviesList = strings;
                mMovieAdapter = new MovieAdapter(MainActivity.this, MainActivity.this, strings);
                mRecyclerView.setAdapter(mMovieAdapter);
                showMovieDataView();
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }

    // Helper method which hides the error message and shows the recyclerview
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(VISIBLE);
    }

    // Helper method which hides the recyclerview and shows the error message
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(VISIBLE);
    }
}
