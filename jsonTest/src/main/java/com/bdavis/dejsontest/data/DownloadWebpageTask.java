package com.bdavis.dejsontest.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by brandondavis on 4/21/14.
 */

public class DownloadWebpageTask extends AsyncTask<String, Void, Book[]> {
    private NetworkListener<Book[]> mListener;
    private String DEBUG_TAG = DownloadWebpageTask.class.getSimpleName();
    public DownloadWebpageTask(NetworkListener<Book[]> listener) {
        mListener = listener;
    }

    @Override
    protected Book[] doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {

            return null;
        }
    }

    @Override
    protected void onPostExecute(Book[] books) {
        if (mListener != null) {
            if (books == null) {
                mListener.onNetworkFailed();
            } else {
                mListener.onNetworkComplete(books);
            }
        }
    }

    private Book[] downloadUrl(String myUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            return readIt(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public Book[] readIt(InputStream stream) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(reader, Book[].class);
    }
}