package com.example.android.booklistingapp;

/**
 * Created by Matthew on 18/06/2017.
 */

public class Book {

    private String bookTitle;
    private String bookAuthor;

    public Book(String thisBookTitle, String thisBookAuthor) {
        bookTitle = thisBookTitle;
        bookAuthor = thisBookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
}
