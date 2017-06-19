package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Matthew on 19/06/2017.
 */

public class StartActivity extends AppCompatActivity {

    private EditText searchQuery;
    private String searchQueryResult;
    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start);

        // find ID for onClickListener
        TextView searchButton = (TextView) findViewById(R.id.search_button);

        searchQuery = (EditText) findViewById(R.id.search_query);

        // set on click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                searchQueryResult = searchQuery.getText().toString();

                if (searchQueryResult.equals("")) {
                    //  A warning message appears if no text is entered.
                    searchQuery.setError(getString(R.string.search_error));
                    return;
                }
                searchTerm = "https://www.googleapis.com/books/v1/volumes?q=" + searchQueryResult + "&maxResults=10";

                Intent searchButtonIntent = new Intent(StartActivity.this, MainActivity.class);
                searchButtonIntent.putExtra("searchTerm", searchTerm);
                startActivity(searchButtonIntent);
            }

        });
    }


    //This method ensures the player's name is saved if the device is rotated
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("searchTerm", searchTerm);
    }

    // This method ensures the player's name is restored from savedInstanceState if the device is rotated.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchTerm = savedInstanceState.getString("searchTerm");
    }
}
