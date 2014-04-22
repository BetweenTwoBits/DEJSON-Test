package com.bdavis.dejsontest.jsontest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.bdavis.dejsontest.adapters.BookAdapter;
import com.bdavis.dejsontest.data.Book;
import com.bdavis.dejsontest.data.DownloadBook;
import com.bdavis.dejsontest.data.NetworkListener;

public class MainActivity extends Activity {
    private static final String booksUrl = "http://de-coding-test.s3.amazonaws.com/books.json";
    private static final String DEBUG_TAG = MainActivity.class.getSimpleName();
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

            new DownloadBook(new NetworkListener<Book[]>() {
                @Override
                public void onNetworkComplete(Book[] result) {
                    mBookAdapter.addAll(result);
                }

                @Override
                public void onNetworkFailed() {
                    Log.e(DEBUG_TAG, "Network failed.");
                }
            }).loadUrl(booksUrl);
        } else {
            Log.e(DEBUG_TAG, "No network connection available");
        }
    }
}