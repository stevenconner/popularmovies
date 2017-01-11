package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String mTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private String mVoteAverage;
    private String mPlotSynopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mReleaseDate = intent.getStringExtra("releaseDate");
        mPosterPath = intent.getStringExtra("posterPath");
        mVoteAverage = intent.getStringExtra("voteAverage");
        mPlotSynopsis = intent.getStringExtra("plot");

        // Set the image on the imageview
        ImageView poster = (ImageView) findViewById(R.id.details_poster_iv);
        Picasso.with(this).load(NetworkUtils.BASE_URL + mPosterPath).into(poster);

        // Set the text to the textviews
        TextView title = (TextView) findViewById(R.id.details_title);
        TextView releaseDate = (TextView) findViewById(R.id.details_release_date);
        TextView voteAverage = (TextView) findViewById(R.id.details_vote_average);
        TextView plot = (TextView) findViewById(R.id.details_plot);

        title.setText(mTitle);
        plot.setText(mPlotSynopsis);
        String releaseDateString = this.getString(R.string.details_release_date) + " " + mReleaseDate;
        String voteAverageString = this.getString(R.string.details_average_vote) + " " + mVoteAverage;
        releaseDate.setText(releaseDateString);
        voteAverage.setText(voteAverageString);
    }
}
