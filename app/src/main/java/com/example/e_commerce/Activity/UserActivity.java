package com.example.e_commerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.e_commerce.Activity.message.ChatMessageActivity;
import com.example.e_commerce.Fragment.CartFragment;
import com.example.e_commerce.Fragment.ProfileFragment;
import com.example.e_commerce.Fragment.ReceiveNotificationFragment;
import com.example.e_commerce.R;
import com.example.e_commerce.Fragment.SearchFragment;
import com.example.e_commerce.Fragment.UserCategoryFragment;
import com.example.e_commerce.Fragment.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        BottomNavigationView bottomNav = findViewById(R.id.user_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_container, new UserHomeFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_header, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.user_header_messenger) {
            Intent intent = new Intent(UserActivity.this, ChatMessageActivity.class);
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.user_header_notification) {
            getSupportFragmentManager().beginTransaction().replace(R.id.user_container
                    , new ReceiveNotificationFragment()).commit();

        }
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;
            if (item.getItemId() == R.id.user_nav_home){
                selected_fragment = new UserHomeFragment();
            } else if(item.getItemId() == R.id.user_nav_category){
                selected_fragment = new UserCategoryFragment();
            } else if(item.getItemId() == R.id.user_nav_search){
                selected_fragment = new SearchFragment();
            } else if(item.getItemId() ==  R.id.user_nav_cart){
                selected_fragment = new CartFragment();
            } else if(item.getItemId() ==  R.id.user_nav_profile){
                selected_fragment = new ProfileFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.user_container
                    , selected_fragment).commit();
            return true;
        }
    };

}