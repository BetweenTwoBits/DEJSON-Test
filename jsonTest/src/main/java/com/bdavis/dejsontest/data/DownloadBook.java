package com.bdavis.dejsontest.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by brandondavis on 4/22/14.
 */

public class DownloadBook extends BaseDownloadTask<Book[]> {

    public DownloadBook(NetworkListener<Book[]> listener) {
        super(listener);
    }

    @Override
    public Book[] readIt(InputStream stream) throws IOException {
            Reader reader;
            reader = new InputStreamReader(stream, "UTF-8");
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, Book[].class);

    }
}