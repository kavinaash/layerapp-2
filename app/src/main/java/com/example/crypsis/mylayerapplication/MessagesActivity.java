package com.example.crypsis.mylayerapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.layer.atlas.AtlasAddressBar;
import com.layer.atlas.AtlasHistoricMessagesFetchLayout;
import com.layer.atlas.AtlasMessageComposer;
import com.layer.atlas.AtlasMessagesRecyclerView;
import com.layer.atlas.AtlasTypingIndicator;
import com.layer.atlas.messagetypes.generic.GenericCellFactory;
import com.layer.atlas.messagetypes.location.LocationCellFactory;
import com.layer.atlas.messagetypes.location.LocationSender;
import com.layer.atlas.messagetypes.singlepartimage.SinglePartImageCellFactory;
import com.layer.atlas.messagetypes.text.TextCellFactory;
import com.layer.atlas.messagetypes.text.TextSender;
import com.layer.atlas.messagetypes.threepartimage.CameraSender;
import com.layer.atlas.messagetypes.threepartimage.GallerySender;
import com.layer.atlas.messagetypes.threepartimage.ThreePartImageCellFactory;
import com.layer.atlas.util.Util;
import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;
import com.layer.sdk.changes.LayerChange;
import com.layer.sdk.changes.LayerChangeEvent;
import com.layer.sdk.exceptions.LayerConversationException;
import com.layer.sdk.listeners.LayerChangeEventListener;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.ConversationOptions;
import com.layer.sdk.messaging.Identity;
import com.layer.sdk.messaging.LayerObject;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.example.crypsis.mylayerapplication.MyApp.layerClient;


/**
 * Created by crypsis on 19/9/16.
 */
public class MessagesActivity extends BaseActivity {
    AtlasMessagesRecyclerView myMessagesList;
    private UiState mState;
    private Conversation mConversation;
    private AtlasAddressBar mAddressBar;
    private AtlasHistoricMessagesFetchLayout mHistoricFetchLayout;
    private AtlasTypingIndicator mTypingIndicator;
    private AtlasMessageComposer mMessageComposer;
    private IdentityChangeListener mIdentityChangeListener;
    Picasso picasso;
//    public MessagesActivity(LauncherActivity launcherActivity, LayerClient layerClient) {
//        ConversationOptions conversationOptions=new ConversationOptions().distinct(false);
//        Conversation conversation=layerClient.newConversationWithUserIds(conversationOptions,"kavi");
//        String messageText = "Hi! How are you";
//        MessagePart messagePart = layerClient.newMessagePart("text/plain", messageText.getBytes());
//        Message message = layerClient.newMessage(Arrays.asList(messagePart));
//        conversation.send(message);
//        List<Uri> l=layerClient.getConversationIds();
//
//
////        List<Conversation> l= layerClient.getConversationsWithParticipants("ZjM1MDkxM2M0MmU2YjI3NQ==","MzJiYTBlNzczMzc5YzU1Nw==");
//        Log.v("convo",l.get(0).toString());
//
//
//
//
//
//
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_layout);

        ConversationOptions conversationOptions=new ConversationOptions().distinct(false);
        Conversation conversation=(getLayerClient()).newConversationWithUserIds(conversationOptions,"kavi");
        String messageText = "Hi! How are you";
        MessagePart messagePart = layerClient.newMessagePart("text/plain", messageText.getBytes());
        Message message = layerClient.newMessage(Arrays.asList(messagePart));
        conversation.send(message);
        final List<Uri> l=layerClient.getConversationIds();


//        List<Conversation> l= layerClient.getConversationsWithParticipants("ZjM1MDkxM2M0MmU2YjI3NQ==","MzJiYTBlNzczMzc5YzU1Nw==");
        Log.v("convo",l.get(0).toString());

        mAddressBar = ((AtlasAddressBar) findViewById(R.id.conversation_launcher))
                .init(getLayerClient(), getPic())
                .setOnConversationClickListener(new AtlasAddressBar.OnConversationClickListener() {
                    @Override
                    public void onConversationClick(AtlasAddressBar addressBar, Conversation conversation) {
                        setConversation(conversation, true);
                        setTitle(true);
                    }
                })
                .setOnParticipantSelectionChangeListener(new AtlasAddressBar.OnParticipantSelectionChangeListener() {
                    @Override
                    public void onParticipantSelectionChanged(AtlasAddressBar addressBar, final List<Identity> participants) {
                        if (participants.isEmpty()) {
                            setConversation(layerClient.getConversation(l.get(0)), false);
                            return;
                        }
                        try {
                            setConversation(getLayerClient().newConversation(new ConversationOptions().distinct(true), new HashSet<>(participants)), false);
                        } catch (LayerConversationException e) {
                            setConversation(e.getConversation(), false);
                        }
                    }
                })
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (mState == UiState.ADDRESS_CONVERSATION_COMPOSER) {
                            mAddressBar.setSuggestionsVisibility(s.toString().isEmpty() ? View.GONE : View.VISIBLE);
                        }
                    }
                })
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            setUiState(UiState.CONVERSATION_COMPOSER);
                            setTitle(true);
                            return true;
                        }
                        return false;
                    }
                });

        mHistoricFetchLayout = ((AtlasHistoricMessagesFetchLayout) findViewById(R.id.historic_sync_layout))
                .init(getLayerClient())
                .setHistoricMessagesPerFetch(20);
        myMessagesList = ((AtlasMessagesRecyclerView) findViewById(R.id.messages_list))
                .init(getLayerClient(), getPic())
                .addCellFactories(
                        new TextCellFactory(),
                        new ThreePartImageCellFactory(this, getLayerClient(), getPic()),
                        new LocationCellFactory(this, getPic()),
                        new SinglePartImageCellFactory(this, getLayerClient(), getPic()),
                        new GenericCellFactory());

        mMessageComposer = ((AtlasMessageComposer) findViewById(R.id.message_composer))
                .init(getLayerClient())
                .setTextSender(new TextSender())
                .addAttachmentSenders(
                        new CameraSender(R.string.attachment_menu_camera, R.drawable.ic_photo_camera_white_24dp, this),
                        new GallerySender(R.string.attachment_menu_gallery, R.drawable.ic_photo_white_24dp, this),
                        new LocationSender(R.string.attachment_menu_location, R.drawable.ic_place_white_24dp, this))
                .setOnMessageEditTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            setUiState(UiState.CONVERSATION_COMPOSER);
                            setTitle(true);
                        }
                    }
                });

    }
    private void setConversation(Conversation conversation, boolean hideLauncher) {
        mConversation = conversation;
        mHistoricFetchLayout.setConversation(conversation);
        myMessagesList.setConversation(conversation);
        mTypingIndicator.setConversation(conversation);
        mMessageComposer.setConversation(conversation);

        // UI state
        if (conversation == null) {
            setUiState(UiState.ADDRESS);
            return;
        }

        if (hideLauncher) {
            setUiState(UiState.CONVERSATION_COMPOSER);
            return;
        }

        if (conversation.getHistoricSyncStatus() == Conversation.HistoricSyncStatus.INVALID) {
            // New "temporary" conversation
            setUiState(UiState.ADDRESS_COMPOSER);
        } else {
            setUiState(UiState.ADDRESS_CONVERSATION_COMPOSER);
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
    public void setTitle(boolean useConversation) {
        if (!useConversation) {
            setTitle(R.string.title_select_conversation);
        } else {
            setTitle(Util.getConversationTitle(getLayerClient(), mConversation));
        }
    }
    private enum UiState {
        ADDRESS,
        ADDRESS_COMPOSER,
        ADDRESS_CONVERSATION_COMPOSER,
        CONVERSATION_COMPOSER
    }
    private void setUiState(UiState state) {
        if (mState == state) return;
        mState = state;
        switch (state) {
            case ADDRESS:
                mAddressBar.setVisibility(View.VISIBLE);
                mAddressBar.setSuggestionsVisibility(View.VISIBLE);
                mHistoricFetchLayout.setVisibility(View.GONE);
                mMessageComposer.setVisibility(View.GONE);
                break;

            case ADDRESS_COMPOSER:
                mAddressBar.setVisibility(View.VISIBLE);
                mAddressBar.setSuggestionsVisibility(View.VISIBLE);
                mHistoricFetchLayout.setVisibility(View.GONE);
                mMessageComposer.setVisibility(View.VISIBLE);
                break;

            case ADDRESS_CONVERSATION_COMPOSER:
                mAddressBar.setVisibility(View.VISIBLE);
                mAddressBar.setSuggestionsVisibility(View.GONE);
                mHistoricFetchLayout.setVisibility(View.VISIBLE);
                mMessageComposer.setVisibility(View.VISIBLE);
                break;

            case CONVERSATION_COMPOSER:
                mAddressBar.setVisibility(View.GONE);
                mAddressBar.setSuggestionsVisibility(View.GONE);
                mHistoricFetchLayout.setVisibility(View.VISIBLE);
                mMessageComposer.setVisibility(View.VISIBLE);
                break;
        }
    }
    private class IdentityChangeListener implements LayerChangeEventListener.Weak {
        @Override
        public void onChangeEvent(LayerChangeEvent layerChangeEvent) {
            // Don't need to update title if there is no conversation
            if (mConversation == null) {
                return;
            }

            for (LayerChange change : layerChangeEvent.getChanges()) {
                if (change.getObjectType().equals(LayerObject.Type.IDENTITY)) {
                    Identity identity = (Identity) change.getObject();
                    if (mConversation.getParticipants().contains(identity)) {
                        setTitle(true);
                    }
                }
            }
        }
    }
}
