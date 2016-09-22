package com.example.crypsis.mylayerapplication;

import android.util.Log;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerConnectionListener;

/**
 * Created by crypsis on 19/9/16.
 */
public class MyConnectionListener implements LayerConnectionListener {
    private LauncherActivity myLauncherActivity;
    private static final String TAG = MyConnectionListener.class.getSimpleName();

    public MyConnectionListener(LauncherActivity LauncherActivity) {
        myLauncherActivity=LauncherActivity;
    }

    @Override
    public void onConnectionConnected(LayerClient layerClient) {
        Log.v(TAG, "Connected to Layer");
        if(!layerClient.isAuthenticated())
        {layerClient.authenticate();}
        else
            myLauncherActivity.startConversation();

    }

    @Override
    public void onConnectionDisconnected(LayerClient layerClient) {
        Log.v(TAG, "Connection to Layer closed");
    }

    @Override
    public void onConnectionError(LayerClient layerClient, LayerException e) {
        Log.v(TAG, "Error connecting to layer: " + e.toString());

    }
}
