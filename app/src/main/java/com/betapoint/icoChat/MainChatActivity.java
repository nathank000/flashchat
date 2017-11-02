package com.betapoint.icoChat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference _dbRef;
    private ChatListAdapter _adapter;

    private static final String TAG = "icoChat:: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        getDisplayName();
        _dbRef = FirebaseDatabase.getInstance().getReference();


        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "handling editor action, event was " +event.toString());

                    sendMessage();
                }
                return true;
            }
        });

        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "send button has been clicked");
                sendMessage();
            }
        });


    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void getDisplayName() {
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString("username", null);
        if (mDisplayName == null) {
            mDisplayName = "Anonymous";
        }
    }

    private void sendMessage() {
        Log.d(TAG, "in send message to send the message editor");
        // TODO: Grab the text the user typed in and push the message to Firebase
        String input = mInputText.getText().toString();

        if (!input.equals("")) {
            Log.d(TAG, "message being sent to the servers");
            InstantMessageModel chat = new InstantMessageModel(input, mDisplayName);
            Log.d(TAG, "chat = " +chat.toString());
            _dbRef.child("messages").push().setValue(chat);
            mInputText.setText("");
        }

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.
    @Override
    public void onStart() {
        super.onStart();
        _adapter = new ChatListAdapter(this, _dbRef, mDisplayName);
        mChatListView.setAdapter(_adapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.
        _adapter.cleanUp();
    }

}
