package com.example.crypsis.mylayerapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.layer.atlas.AtlasConversationsRecyclerView;
import com.layer.atlas.adapters.AtlasConversationsAdapter;
import com.layer.atlas.util.Log;
import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;
import com.layer.atlas.util.views.SwipeableItem;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Conversation;
import com.squareup.picasso.Picasso;

/**
 * Created by crypsis on 23/9/16.
 */

public class ConversationListActivity extends BaseActivity {
    AtlasConversationsRecyclerView mConversationsList;
    Picasso picasso;
    private static final int RESULT_PICK_CONTACT = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_list);
        mConversationsList = (AtlasConversationsRecyclerView) findViewById(R.id.conversations_list);

        // Atlas methods
        mConversationsList.init(getLayerClient(), getPic())
                .setInitialHistoricMessagesToFetch(20)
                .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
                    @Override
                    public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        Intent intent = new Intent(ConversationListActivity.this, MessagesActivity.class);
                        if (Log.isLoggable(Log.VERBOSE)) {
                            Log.v("Launching MessagesListActivity with existing conversation ID: " + conversation.getId());
                        }
                        intent.putExtra("ConversationID", conversation.getId());
                        startActivity(intent);
                    }

                    @Override
                    public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        return false;
                    }
                }) .setOnConversationSwipeListener(new SwipeableItem.OnSwipeListener<Conversation>() {
            @Override
            public void onSwipe(final Conversation conversation, int direction) {
                new AlertDialog.Builder(ConversationListActivity.this)
                        .setMessage(R.string.alert_message_delete_conversation)
                        .setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: simply update this one conversation
                                mConversationsList.getAdapter().notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton(R.string.alert_button_delete_my_devices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                conversation.delete(LayerClient.DeletionMode.ALL_MY_DEVICES);
                            }
                        })
                        .setPositiveButton(R.string.alert_button_delete_all_participants, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                conversation.delete(LayerClient.DeletionMode.ALL_PARTICIPANTS);
                            }
                        })
                        .show();
            }
        });
        findViewById(R.id.floating_action_button)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
//                        startActivity(new Intent(ConversationListActivity.this, MessagesActivity.class));
                        Intent contactPickerIntent= new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                    }

                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {

        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            name = cursor.getString(nameIndex);
            Intent intent=new Intent(ConversationListActivity.this,NewConversationActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Picasso getPic() {
        if (picasso == null) {
            // Picasso with custom RequestHandler for loading from Layer MessageParts.
            picasso = new Picasso.Builder(this)
                    .addRequestHandler(new MessagePartRequestHandler(getLayerClient()))
                    .build();
        }
        return picasso;
    }
}
