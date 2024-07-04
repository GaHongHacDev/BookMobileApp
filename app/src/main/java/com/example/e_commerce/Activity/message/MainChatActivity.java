package com.example.e_commerce.Activity.message;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Common.ApplicationUser;
import com.example.e_commerce.Common.GlobalConfig;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainChatActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    DatabaseReference databaseReference;
    private String mobile;
    private String email;
    private String name;
    private RecyclerView messagesRecyclerView;
    private String chatKey = "";
    private String lastMessage = "";
    private int unseenMessages = 0;
    private boolean dataSet = false;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        messagesLists.clear();

        databaseReference
                = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(GlobalConfig.REFERENCE_FROM_URL);

        final ImageView userProfilePic = findViewById(R.id.userProfilePic);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        User currentUser = ApplicationUser.getUserFromSharedPreferences(this);

        mobile = currentUser.getPhone_number();
        email = currentUser.getEmail();
        name = currentUser.getUsername();

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set adapter to recycle view
        messagesAdapter = new MessagesAdapter(messagesLists, MainChatActivity.this);

        messagesRecyclerView.setAdapter(messagesAdapter);


        //set up user info in chat screen
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    final String profilePicUrl = snapshot.child("users-chat")
                            .child(mobile)
                            .child("profile_pic")
                            .getValue(String.class);

                    if (!profilePicUrl.isEmpty()) {
                        Picasso.get().load(profilePicUrl).into(userProfilePic);
                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear(); // Clear the existing list to prevent duplicates

                for (DataSnapshot dataSnapshot : snapshot.child("users-chat").getChildren()) {
                    final String receiveMobile = dataSnapshot.getKey();
                    dataSet = false;

                    if (!receiveMobile.equals(mobile)) {
                        final String receiveName = dataSnapshot.child("name").getValue(String.class);
                        final String receiveProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);

                        // Check if the user already exists in the list by comparing receiveMobile
                        boolean userExists = false;
                        for (MessagesList messagesList : messagesLists) {
                            if (messagesList.getMobile().equals(receiveMobile)) {
                                // User already exists, update their information
                                userExists = true;
                                messagesList.setName(receiveName);
                                messagesList.setProfilePic(receiveProfilePic);
                                break;
                            }
                        }

                        if (!userExists) {
                            MessagesList messagesList = new MessagesList(
                                    receiveName, receiveMobile, lastMessage, receiveProfilePic, unseenMessages, chatKey);
                            messagesLists.add(messagesList);
                        }
                    }
                }

                messagesAdapter.updateData(messagesLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

}