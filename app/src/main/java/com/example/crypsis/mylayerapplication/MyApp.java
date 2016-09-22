package com.example.crypsis.mylayerapplication;

import android.app.Application;
import android.content.Context;

import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;
import com.layer.sdk.LayerClient;
import com.squareup.picasso.Picasso;

/**
 * Created by crypsis on 19/9/16.
 */
public class MyApp extends Application {
    static  LayerClient layerClient;
    public final static String LAYER_APP_ID = "layer:///apps/staging/170be0e8-6846-11e6-a7a9-d9a4c50e244c";

    // Set your Google Cloud Messaging Sender ID from your Google Developers Console.
    private final static String GCM_SENDER_ID = "24320527281";

    Picasso sPicasso;
     Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        LayerClient.applicationCreated(this);
        context=this;
    }
public LayerClient getlayerClient(){
    return layerClient;
}
    public  LayerClient setLayerClient(LayerClient mylayerClient){layerClient=mylayerClient;
    return layerClient;}
public  Context getContext()
{
    return context;
}

    public  LayerClient getLayerClient()
    {
        return layerClient;
    }
    public  Picasso getPicasso() {
        if (sPicasso == null) {
            // Picasso with custom RequestHandler for loading from Layer MessageParts.
            sPicasso = new Picasso.Builder(context)
                    .addRequestHandler(new MessagePartRequestHandler(getLayerClient()))
                    .build();
        }
        return sPicasso;
    }
}
