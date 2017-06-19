package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

/**
 * Created by Matthew on 18/06/2017.
 */

public class QueryUtils {

    // Tag for log messages to identify errors
    private static final String LOG_TAG = QueryUtils.class.getName();

    // create an empty constructor
    private QueryUtils() {
    }

    // Public method that brings everything together to create a list of books
    public static List<Book> fetchBookData(String requestUrl) {

        // create the URL
        URL url = createUrl(requestUrl);

        // send HTTP request to server
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // process the response
        List<Book> books = extractBooks(jsonResponse);
        // return the list of books
        return books;
    }

    // private method for creating URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            // generic catch method for URLs
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        // return URL for public method
        return url;
    }

    // private method for making HTTP Request and returning a jsonResponse String
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if the URL is null, return early
        if (url == null) {
            return jsonResponse;
        }

        // otherwise process with rest of method
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect(); //at this point, the URL connection is made

            // if URL connection is successful (result 200) proceed; else throw error
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Uh, oh - error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the data.", e);
        } finally {
            // if URL connection is made and data retrieved, disconnect and close input stream
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        // return jsonResponse for public method
        return jsonResponse;
    }

    // use StringBuilder and Buffer reader to read and compute the String, so as to minimise use of resources
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

    // method to use data required from API
    private static List<Book> extractBooks(String bookJSON) {

        // return early if the JSON string is empty
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // create an empty ArrayList that books can be added to
        List<Book> books = new ArrayList<>();

        // Parse the response from the URL; throw exception if there is a problem and report to logs
        try {
            JSONObject jsonObject = new JSONObject(bookJSON);
            // extract 'items' from JSONArray
            JSONArray bookArray = jsonObject.getJSONArray("items");
            // Loop through each item in the items array
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);

                // get title for each item in array list
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String bookTitle = volumeInfo.getString("title");

                // Create StringBuilder for list of authors
                StringBuilder bookAuthor = new StringBuilder();

                // Check if authors exist
                if (volumeInfo.has("authors")) {

                    // find Array within current array to return list of authors
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");

                    // Loop through each item in the authors array
                    for (int j = 0; j < authorsArray.length(); j++) {
                        // create new line for each author
                        bookAuthor.append(System.getProperty("line.separator"));
                        bookAuthor.append(authorsArray.getString(j));

                    }
                    // return placeholder for when author doesn't exist
                } else {
                    bookAuthor.append(R.string.no_author);
                }

                // check if the book has a description
                String bookDescription;
                if(volumeInfo.has("description")) {
                    bookDescription = volumeInfo.getString("description");
                } else {
                    // TODO: change to Resource String ID
                    bookDescription = "No descripton available";
                }


                // get JSONObject accessInfo
                JSONObject accessInfo = currentBook.getJSONObject("accessInfo");
                // get URL for web link for each item
                String webLink;
                // Check if web reader link exists
                if (accessInfo.has("webReaderLink")) {
                    // get URL for web link
                    webLink = accessInfo.getString("webReaderLink");
                } else {
                    webLink = null;
                }


                // create new book
                Book book = new Book(bookTitle, bookAuthor, bookDescription, webLink);
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Book API results.", e);
        }

        // return the list of books
        return books;
    }
}
