package com.example.android.booklistingapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private EditText searchQuery;
    private TextView emptyStateTextView;
    private ProgressBar progressBar;
    private ListView bookListView;
    private BookAdapter adapter;
    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create toolbar menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // restore saved search term
        searchTerm = getIntent().getExtras().getString("searchTerm");

        // find ids for displaying progress bar and error when no results found
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Find id for view to populate with results
        bookListView = (ListView) findViewById(R.id.list);

        // Set the view to empty text view if no results are found
        bookListView.setEmptyView(emptyStateTextView);

        // Create a new ArrayAdapter of books
        adapter = new BookAdapter(this, new ArrayList<Book>());
        // set the adapter to the already identified list
        bookListView.setAdapter(adapter);

        // Create onItemClickListener for each book
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // find the current button that was clicked on
                Book currentBook = adapter.getItem(position);

                if (currentBook.getWebLink() != null) {
                    // Convert the string url into a URL object that can be passed to an intent
                    Uri bookUri = Uri.parse(currentBook.getWebLink());

                    // Create new intent to launch web browser
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                    // start the intent
                    startActivity(webIntent);
                }
            }

        });

        // Check there is a network connection before proceeding
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if there is a network connection or the connection is active proceed with the loader
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(0, null, MainActivity.this);
            // if not, hide progress bar and display network error
        } else {
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_network);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        // TODO: set up proper process to return API address, using EditText to string
        return new BookLoader(this, searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // set EmptyTextState to 'no books found'
        emptyStateTextView.setText(R.string.empty_view);

        // Hide Progress Bar
        progressBar.setVisibility(View.GONE);

        // clear the adapter of previous book data
        adapter.clear();

        // return a list of books based on user input
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
}

