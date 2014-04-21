package com.bdavis.dejsontest.jsontest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;

public class MainActivity extends Activity {

    private static final String booksUrl = "http://de-coding-test.s3.amazonaws.com/books.json";
    private static final String DEBUG_TAG = "HttpExample";
    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionCheck();

        ListView listView = (ListView) findViewById(R.id.book_list);
        mBookAdapter = new BookAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mBookAdapter);

    }

    public void connectionCheck() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(booksUrl);
        } else {
            Log.e(DEBUG_TAG, "No network connection available");
        }

    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, Book[]> {
        @Override
        protected Book[] doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                Log.e(DEBUG_TAG, "Exception:" + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Book[] books) {
            mBookAdapter.addAll(books);
        }
    }

    private Book[] downloadUrl(String myUrl) throws IOException {
        InputStream is = null;

        try{
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

    private class Book {
        private String title;
        private String imageURL;
        private String author;

        public String getTitle() {
            return title;
        }
        public String getImageUrl() {
            return imageURL;
        }
        public String getAuthor() { return author; }
    }

    private class BookAdapter extends ArrayAdapter<Book> {

        public BookAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout bookItem = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
            TextView title = (TextView) bookItem.findViewById(R.id.title);
            TextView author = (TextView) bookItem.findViewById(R.id.author);
            ImageView cover = (ImageView) bookItem.findViewById(R.id.cover);

            title.setText(getItem(position).getTitle());

            if(getItem(position).getAuthor() != null){
                author.setText("Author: " + getItem(position).getAuthor());
            }

            Ion.with(getContext())
                    .load(getItem(position).getImageUrl())
                    .withBitmap()
                    .placeholder(R.drawable.ic_launcher)
                    .intoImageView(cover);

            return bookItem;
        }
    }
}




