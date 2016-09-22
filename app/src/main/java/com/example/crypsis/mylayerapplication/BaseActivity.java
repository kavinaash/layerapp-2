package com.example.crypsis.mylayerapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.layer.sdk.LayerClient;
import com.squareup.picasso.Picasso;

/**
 * Created by crypsis on 20/9/16.
 */

public class BaseActivity extends AppCompatActivity {
MyApp myApp=new MyApp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
   public LayerClient getLayerClient()
   {
       return myApp.getLayerClient();
   }
    public Picasso getPicasso(){
        return myApp.getPicasso();
    }
}
