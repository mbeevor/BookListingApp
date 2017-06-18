package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matthew on 18/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        Book book = getItem(position);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        bookTitle.setText(book.getBookTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.book_author);
        bookAuthor.setText(book.getBookAuthor());

        return listItemView;
    }
}
