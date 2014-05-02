package com.bdavis.dejsontest.jsontest;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.bdavis.dejsontest.adapters.CursorBookAdapter;
import com.bdavis.dejsontest.data.Book;
import com.bdavis.dejsontest.data.DatabaseHandler;
import com.bdavis.dejsontest.data.DownloadBook;
import com.bdavis.dejsontest.data.NetworkListener;

public class MainActivity extends ListActivity {
    private static final String booksUrl = "http://de-coding-test.s3.amazonaws.com/books.json";
    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteDatabase db = null;
    private DatabaseHandler dbHandler = new DatabaseHandler(this);
    private CursorBookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        db = new DatabaseHandler(this).getWritableDatabase();

        connectionCheck();

        Cursor cursor = dbHandler.allRowsToCursor();

        mBookAdapter = new CursorBookAdapter(this,
                R.layout.book_item,
                cursor,
                new String[]{"title", "author"},
                new int[]{R.id.title, R.id.author},
                0);
        setListAdapter(mBookAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void connectionCheck() {
        //start new connection manager
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //get connection info from connection manager
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            new DownloadBook(new NetworkListener<Book[]>() {
                @Override
                public void onNetworkComplete(Book[] result) {
                    dbHandler.insertBooks(result);
                    Cursor cursor = dbHandler.allRowsToCursor();
                    mBookAdapter.swapCursor(cursor);
                }

                @Override
                public void onNetworkFailed() {
                    Log.e(TAG, "Network failed.");
                }
            }).loadUrl(booksUrl);
        } else {
            Log.e(TAG, "No network connection available");
        }
    }
}