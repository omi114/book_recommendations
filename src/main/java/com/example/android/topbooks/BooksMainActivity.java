package com.example.android.topbooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BooksMainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>{

    private String API_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    //private String API_URL = "https://www.googleapis.com/books/v1/volumes?q=android";

    private int LOADER_ID=1;

    ArrayAdapter<Book> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_main);

        // List view of books
        ListView bookListView = (ListView) findViewById(R.id.list);
        // Set empty view as an alternative to list view
        //TODO: bookListView.setEmptyView(emptyTextView);

        // Create a new {@link ArrayAdapter} of from a new ArrayList
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        // What happens when an item is clicked
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Book clickedBook = mAdapter.getItem(i);
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedBook.getUrl()));
                startActivity(urlIntent);
            }
        });

        //Check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
        //else {
        //    mAdapter.clear();
        //    progressBar.setVisibility(View.GONE);
        //    emptyTextView.setText(R.string.no_internet);
        //}

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        return new BookLoader(this, API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        if (books != null && books.size() > 0) {
            if(mAdapter.getCount() > 0) {
                return;
            }
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();

    }
}
