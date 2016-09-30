package com.example.crypsis.mylayerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.layer.atlas.AtlasMessageComposer;
import com.layer.atlas.AtlasMessagesRecyclerView;
import com.layer.sdk.messaging.Conversation;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by crypsis on 26/9/16.
 */

public class NewConversationActivity extends BaseActivity {
    private Conversation mConversation;
    AtlasMessagesRecyclerView myMessagesList;
    AtlasMessageComposer messageComposer;
    Retrofit retrofit;
    public static final String BASE_URL = " https://api.layer.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_layout);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myMessagesList=(AtlasMessagesRecyclerView)findViewById(R.id.messages_list);
        messageComposer=(AtlasMessageComposer)findViewById(R.id.message_composer);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        ServerApi serverApi=retrofit.create(ServerApi.class);
        NewUserModel newUserModel=new NewUserModel(getLayerClient().getAppId().toString(),name,true);
        Call<Conversation> call=serverApi.createUser(newUserModel);
        call.enqueue(new Callback<Conversation>() {
                         @Override
                         public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                             Conversation conversation=response.body();
                             setConvo(conversation,true);
                         }

                         @Override
                         public void onFailure(Call<Conversation> call, Throwable t) {

                         }
                     });


//        ConversationOptions conversationOptions=new ConversationOptions().distinct(false);
//        Conversation conversation=(getLayerClient()).newConversationWithUserIds(conversationOptions,name);
//        String messageText = "Hi! How are you";
//        MessagePart messagePart = layerClient.newMessagePart("text/plain", messageText.getBytes());
//        Message message = layerClient.newMessage(Arrays.asList(messagePart));
//        conversation.send(message);
//       final List<Uri> l=layerClient.getConversationIds();
//        Conversation c=layerClient.getConversation(l.get(0));
//        setConvo(c,true);
    }
    private void setConvo(Conversation conversation, boolean hideLauncher) {
        mConversation = conversation;

        myMessagesList.setConversation(conversation);
//
        messageComposer.setConversation(conversation);

        // UI state
//
    }
}
