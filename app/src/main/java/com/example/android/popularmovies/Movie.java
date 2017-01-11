package com.example.android.popularmovies;

/**
 * {@link Movie} represents a single movie object for the MainActivity data.
 * It contains the title, release date, movie poster, vote average, and plot synopsis.
 */

public class Movie {
    // Title of the movie
    private String mTitle;
    // Release date of the movie
    private String mReleaseDate;
    // Poster path for the movie
    private String mPosterPath;
    // Vote average rating for the movie
    private String mVoteAverage;
    // Plot synopsis for the movie
    private String mPlotSynopsis;

    public Movie(String title, String releaseDate, String posterPath, String voteAverage, String plotSynopsis) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
    }

    // Getter methods for each variable
    public String getmTitle() { return mTitle; }
    public String getmReleaseDate() { return mReleaseDate; }
    public String getmPosterPath() { return mPosterPath; }
    public String getmVoteAverage() { return mVoteAverage; }
    public String getmPlotSynopsis() { return mPlotSynopsis; }

}
