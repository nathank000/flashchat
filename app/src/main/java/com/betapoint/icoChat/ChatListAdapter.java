package com.betapoint.icoChat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by m50571 on 10/27/17.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity _activity;
    private DatabaseReference _dbRef;
    private String _displayName;
    private ArrayList<DataSnapshot> _snapshotList;

    private ChildEventListener _listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("FIREBASE LISTENER", "in onChildAdded");
            _snapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String username) {
        _activity = activity;
        _displayName = username;
        _dbRef = ref.child("messages");
        _dbRef.addChildEventListener(_listener);
        _snapshotList = new ArrayList<>();

    }


    //////////////////////////////////////////////////////////////
    // static classes holder
    //////////////////////////////////////////////////////////////

    static class ViewHolder {
        TextView authorName;
        TextView msgBody;
        LinearLayout.LayoutParams params;
    }


    @Override
    public int getCount() {
        return _snapshotList.size();
    }

    @Override
    public InstantMessageModel getItem(int position) {

        DataSnapshot snapshot = _snapshotList.get(position);

        return snapshot.getValue(InstantMessageModel.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("GET VIEW", "in getView, incoming view = " + convertView);

        if (convertView == null) {
            Log.d("GET VIEW", "creating a new view");

            //create an inflater
            LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //create a view from the xml
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            //create a new view holder to hold the view items for the moment
            //hold view objects in a view holder
            //https://developer.android.com/training/improving-layouts/smooth-scrolling.html
            final ViewHolder holder = new ViewHolder();

            //set the vars
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.msgBody = (TextView) convertView.findViewById(R.id.message);

            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final InstantMessageModel instantMessage = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        boolean isMe = instantMessage.getAuthor().equals(_displayName);
        setChatRowAppearance(isMe, viewHolder);

        //get the author from the IM model & set it as the text of the textfield
        String author = instantMessage.getAuthor();
        viewHolder.authorName.setText(author);

        //get the message text from the IM model and set it to the message of the message body text view
        String msg = instantMessage.getMessage();
        viewHolder.msgBody.setText(msg);



        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {
        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.msgBody.setBackgroundResource(R.drawable.bubble2);
        }
        else
        {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.msgBody.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.msgBody.setLayoutParams(holder.params);
    }

    public void cleanUp() {
        _dbRef.removeEventListener(_listener);
    }
}
