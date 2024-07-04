package com.example.e_commerce.Activity.message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Common.ApplicationUser;
import com.example.e_commerce.Common.GlobalConfig;
import com.example.e_commerce.Database.Database;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatMessageActivity extends AppCompatActivity {

    private List<ChatList> chatLists = new ArrayList<>();
    ImageView btnSendMessage, profilePic;
    EditText messageEditTxt;
    TextView name;
    DatabaseReference databaseReference;
    String chatKey;
    String getUserMobile = "";
    String receiveMobile = "", receiveName = "", receiveProfilePic = "";
    RecyclerView chattingRecycleView;
    ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        // Initialize database firebase
        databaseReference
                = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(GlobalConfig.REFERENCE_FROM_URL);

        binding();

        receiveName
                = getIntent().getStringExtra("name");
        receiveProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        receiveMobile = getIntent().getStringExtra("mobile");

        //get user mobile from memory
        User currentUser = ApplicationUser.getUserFromSharedPreferences(this);

        if (!currentUser.getEmail().equals("admin")){
            chatKey = currentUser.getPhone_number();
            receiveName = "GoodTome";
            receiveProfilePic = "https://i.pinimg.com/originals/7e/ce/c4/7ecec434137d1fcbe023db38e06c1260.jpg";
            receiveMobile = "0000000000";

        }
        getUserMobile = currentUser.getPhone_number();

        name.setText(receiveName);
        Picasso.get().load(receiveProfilePic).into(profilePic);

        chattingRecycleView.setHasFixedSize(true);
        chattingRecycleView.setLayoutManager(new LinearLayoutManager(ChatMessageActivity.this));

        chatAdapter = new ChatAdapter(chatLists, ChatMessageActivity.this);

        chattingRecycleView.setAdapter(chatAdapter);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    //generate chat key by default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }

                }
                if (snapshot.hasChild("chat")){
                    if (snapshot.child("chat").child(chatKey).hasChild("messages")){
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("chat")
                                .child(chatKey).child("messages")
                                .getChildren()){

                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String receiveMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                                SimpleDateFormat simpleTimeFormat =
                                        new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                ChatList chatList = new ChatList(receiveMobile, receiveName
                                        , getMsg
                                        , simpleDateFormat.format(date)
                                        , simpleTimeFormat.format(date));

                                /*if (loadingFirstTime *//*|| Long.parseLong(messageTimestamps) > 1*//*){
                                    //save lastMessage*/

                                    loadingFirstTime = false;

                                    chatLists.add(chatList);

                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecycleView.scrollToPosition(chatLists.size() - 1);

                                //}

                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This code will be triggered whenever there is a change in the data
                if (chatKey.isEmpty()) {
                    // generate chat key by default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }
                if (snapshot.hasChild("chat")) {
                    if (snapshot.child("chat").child(chatKey).hasChild("messages")) {
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("chat")
                                .child(chatKey).child("messages")
                                .getChildren()) {
                            // Your existing code to populate chatLists
                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")) {
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String receiveMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                // Parse timestamp
                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                                SimpleDateFormat simpleTimeFormat =
                                        new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                ChatList chatList = new ChatList(receiveMobile, receiveName
                                        , getMsg
                                        , simpleDateFormat.format(date)
                                        , simpleTimeFormat.format(date));

                                chatLists.add(chatList);
                            }
                        }
                        // Update the UI with chatLists after processing the data
                        chatAdapter.updateChatList(chatLists);
                        chattingRecycleView.scrollToPosition(chatLists.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });



        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                final String getTextMessage = messageEditTxt.getText().toString();
                databaseReference.child("chat").child(chatKey)
                        .child("user_1").setValue(getUserMobile);

                databaseReference.child("chat").child(chatKey)
                        .child("user_2").setValue(receiveMobile);

                databaseReference.child("chat").child(chatKey)
                        .child("messages").child(currentTimestamp)
                        .child("msg").setValue(getTextMessage);

                databaseReference.child("chat").child(chatKey)
                        .child("messages").child(currentTimestamp)
                        .child("mobile").setValue(getUserMobile);

                messageEditTxt.setText("");

            }
        });

    }



    private void binding() {

        btnSendMessage = findViewById(R.id.btnSendMessage);
        profilePic = findViewById(R.id.profilePic);
        messageEditTxt = findViewById(R.id.messageEditTxt);
        name = findViewById(R.id.name);
        chattingRecycleView = findViewById(R.id.chattingRecycleView);

    }
}