package com.example.android.topbooks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by omkarpathak on 4/5/18.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        try {
            return QueryUtils.getBooks(mUrl);
        } catch (IOException e) {
            Log.e("loadInBackground","Error in Getting Books");
            return null;
        }

    }
}
