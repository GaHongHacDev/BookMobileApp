package com.example.e_commerce.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Adapter.NotificationAdapter;
import com.example.e_commerce.Common.ApplicationUser;
import com.example.e_commerce.Common.GlobalConfig;
import com.example.e_commerce.Model.NotificationModel;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReceiveNotificationFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference databaseReference;
    private ListView listView;
    private NotificationAdapter adapter;
    private List<NotificationModel> list;
    private String mobileTxt;
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReceiveNotificationFragment() {

    }

    public static ReceiveNotificationFragment newInstance(String param1, String param2) {
        ReceiveNotificationFragment fragment = new ReceiveNotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        databaseReference
                = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(GlobalConfig.REFERENCE_FROM_URL);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_receive_notification
                , container, false);

        listView = v.findViewById(R.id.listViewNotifications);
        adapter = new NotificationAdapter(list, getActivity());
        listView.setAdapter(adapter);

        User currentUser = ApplicationUser.getUserFromSharedPreferences(getActivity());
        mobileTxt = currentUser.getPhone_number();

        databaseReference.child("notification").child(mobileTxt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear(); // Clear the existing list

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    final String notificationTimestamps = dataSnapshot1.getKey();
                    final String receiveTitle = dataSnapshot1.child("title").getValue(String.class);
                    final String receiveContent = dataSnapshot1.child("content").getValue(String.class);
                    NotificationModel notification = new NotificationModel(receiveTitle, receiveContent);
                    if (notification != null) {
                        list.add(notification);
                    }
                }
                adapter.updateNotification(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


        return v;
    }

}