package com.bdavis.dejsontest.data;

/**
 * Created by brandondavis on 4/21/14.
 */
public interface NetworkListener<T> {
    public void onNetworkComplete(T result);
    public void onNetworkFailed();
}
