package com.example.android.booklistingapp;

/**
 * Created by Matthew on 18/06/2017.
 */

public class Book {

    private String bookTitle;
    private StringBuilder bookAuthor;
    private String bookDescription;
    private String webLink;

    public Book(String thisBookTitle, StringBuilder thisBookAuthor, String description, String webReaderLink) {
        bookTitle = thisBookTitle;
        bookAuthor = thisBookAuthor;
        bookDescription = description;
        webLink = webReaderLink;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public StringBuilder getBookAuthor() {
        return bookAuthor;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getWebLink() {
        return webLink;
    }

}
