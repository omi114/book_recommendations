package com.example.android.topbooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by omkarpathak on 4/5/18.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> mBooks ;
    public BookAdapter (Context context, ArrayList<Book> books){

        super(context, 0, books);
        mBooks = books;

    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (convertView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book, parent, false);
        }

        Book book = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(book.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText(book.getAuthor());

        return listItemView;
    }
}
