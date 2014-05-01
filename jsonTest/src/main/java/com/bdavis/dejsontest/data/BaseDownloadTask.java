package com.bdavis.dejsontest.data;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseDownloadTask<T>{
    private NetworkListener<T> mListener;
    private String TAG = BaseDownloadTask.class.getSimpleName();

    public BaseDownloadTask(NetworkListener<T> listener) {
        mListener = listener;
    }

    private class networkTask extends AsyncTask<String, Void, T> {
        @Override
        protected T doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(T t) {
            if (mListener != null) {
                if (t == null) {
                    mListener.onNetworkFailed();
                } else {
                    mListener.onNetworkComplete(t);
                }
            }
        }

        private T downloadUrl(String myUrl) throws IOException {
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
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                return readIt(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    public abstract T readIt(InputStream stream) throws IOException;

    public void loadUrl(String url) {
        new networkTask().execute(url);
    }
}