package com.example.android.popularmovies.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * These utilities will be used to communicate with theMovieDB
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_URL = "http://image.tmdb.org/t/p/w500/";

    // this is the API key we will use to access theMovieDB
    // TODO: Change this API Key to your own key
    private static final String API_KEY = "843d291cdd0a192bb997da4c2872852e";

    // Constant strings for URLs we will use to display the movies on the main activity.
    private static final String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    private static final String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;

    /**
     * This method returns the entire result from an HTTP response.
     *
     * @param url   The URL to fetch the HTTP response from.
     * @return      The contents of the HTTP response.
     * @throws      java.io.IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String jsonResponse = "";

        // If url is null, return an empty string early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // Then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem receiving the JSON Response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from
     * the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    // Return a list of {@link Movie} objects that has been built up from parsing a JSON response.
    public static ArrayList<Movie> extractFeatureFromJson(String moviesJson){
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(moviesJson)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding movie objects to
        ArrayList<Movie> movies = new ArrayList<>();

        /**
         * Try to parse the JSON. If there's a problem with the way the JSON is formatted, a
         * JSONException exception object will be thrown. Catch the exception so the app doesn't
         * crash, and print the error message to the logs.
         */
        try {
            // Create a new JSONObject named root which uses JSON from theMovieDB.
            JSONObject root = new JSONObject(moviesJson);

            // Extract the "results" array from the root JSONObject
            JSONArray results = root.getJSONArray("results");

            // Loop through each JSONObject in the results array, add it to the ArrayList
            for (int i = 0; i < results.length(); i++) {
                // Collect the JSONObject
                JSONObject movie = results.getJSONObject(i);
                // Extract the portions of the result that we care about
                String title = movie.getString("title");
                String releaseDate = movie.getString("release_date");
                String posterPath = movie.getString("poster_path");
                String voteAverage = movie.getString("vote_average");
                String plotSynopsis = movie.getString("overview");

                // Add the movie to the ArrayList
                movies.add(new Movie(title, releaseDate, posterPath, voteAverage, plotSynopsis));
            }

        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing theMovieDB JSON results", e);
        }
        return movies;
    }

    // Methods to return based on sort preferences, takes a boolean sortPreference as input to
    // figure out which URL to query the server with.
    public static ArrayList<Movie> fetchMoviesData(Boolean sortPreference) {
        URL url = null;
        String jsonResponse = null;
        String urlToUse;
        // If sortType is true, sort by popularity, if false, sort by top rated
        if (sortPreference) {
            urlToUse = MOST_POPULAR_URL;
        } else {
            urlToUse = TOP_RATED_URL;
        }
        try {
            url = new URL(urlToUse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            try {
                jsonResponse = getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        ArrayList<Movie> movies = extractFeatureFromJson(jsonResponse);

        // Returns the list of movies
        return movies;
    }
}
