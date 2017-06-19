package com.example.android.booklistingapp;

/**
 * Created by Matthew on 18/06/2017.
 */

public class Book {

    private String bookTitle;
    private StringBuilder bookAuthor;

    public Book(String thisBookTitle, StringBuilder thisBookAuthor) {
        bookTitle = thisBookTitle;
        bookAuthor = thisBookAuthor;
    }

    public Book(String thisBookTitle) {
        bookTitle = thisBookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public StringBuilder getBookAuthor() {
        return bookAuthor;
    }
}
