package com.bdavis.dejsontest.data;

public interface NetworkListener<T> {
    public void onNetworkComplete(T result);
    public void onNetworkFailed();
}
