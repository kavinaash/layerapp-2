package com.example.crypsis.mylayerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.layer.sdk.LayerClient;

public class LauncherActivity extends BaseActivity  {


    public final static String LAYER_APP_ID = "layer:///apps/staging/170be0e8-6846-11e6-a7a9-d9a4c50e244c";

    // Set your Google Cloud Messaging Sender ID from your Google Developers Console.
    private final static String GCM_SENDER_ID = "24320527281";
    ProgressBar progressBar;
    LayerClient layerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
       layerClient= getLayerClient();
        layerClient = LayerClient.newInstance(this, LAYER_APP_ID,
                new LayerClient.Options().googleCloudMessagingSenderId(GCM_SENDER_ID));

        layerClient.connect();
        MyConnectionListener myConnectionListener = new MyConnectionListener(this);
        MyAuthenticationListener myAuthenticationListener = new MyAuthenticationListener(this);
        layerClient.registerConnectionListener(myConnectionListener);
        layerClient.registerAuthenticationListener(myAuthenticationListener);
        MyApp.layerClient=layerClient;

    }



    public void startConversation() {
        progressBar.setVisibility(View.GONE);
//        MessagesActivity messagesActivity = new MessagesActivity(this, layerClient);

        Intent intent=new Intent(LauncherActivity.this,ConversationListActivity.class);
        startActivity(intent);
    }

    public String getDeviceID() {
        return  "avi";
//        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        return Base64.encodeToString(androidId.getBytes(), Base64.NO_WRAP);
    }


}
