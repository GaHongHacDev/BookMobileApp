package com.example.e_commerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.e_commerce.Activity.message.MainChatActivity;
import com.example.e_commerce.Fragment.ChartFragment;
import com.example.e_commerce.Fragment.FeedbackFragment;
import com.example.e_commerce.Fragment.ManageCategoryFragment;
import com.example.e_commerce.Fragment.ManageGoodTomeFragment;
import com.example.e_commerce.Fragment.ManageOrderFragment;
import com.example.e_commerce.Fragment.ManageProductFragment;
import com.example.e_commerce.R;
import com.example.e_commerce.Fragment.ReportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    Fragment admin_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
//        if(id == 1)
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageOrderFragment()).commit();
//        else if (id == 2) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageCategoryFragment()).commit();
//            Toast.makeText(AdminActivity.this, "Chỉnh sửa thể loại thành công"
//                    , Toast.LENGTH_LONG).show();
//        } else if (id == 3) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageCategoryFragment()).commit();
//            Toast.makeText(AdminActivity.this, "Tạo thể loại thành công"
//                    , Toast.LENGTH_LONG).show();
//        } else if (id == 4) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageProductFragment()).commit();
//            Toast.makeText(AdminActivity.this, "Chỉnh sửa sách thành công"
//                    , Toast.LENGTH_LONG).show();
//        }  else if (id == 5) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageProductFragment()).commit();
//            Toast.makeText(AdminActivity.this, "Tạo sách thành công"
//                    , Toast.LENGTH_LONG).show();
//        } else if (id == 6) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageOrderFragment()).commit();
//        } else if (id == 7) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageCategoryFragment()).commit();
//        }

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navAdminListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.admin_container, new ManageOrderFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.nav_manage_product) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageProductFragment()).commit();
//        } else if (item.getItemId() == R.id.nav_manage_category) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageCategoryFragment()).commit();
//        } else if (item.getItemId() == R.id.nav_order) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
//                    , new ManageOrderFragment()).commit();
//        }else
        if (item.getItemId() == R.id.nav_report) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
                    , new ReportFragment()).commit();
        } else if (item.getItemId() == R.id.nav_feedback) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
                    , new FeedbackFragment()).commit();
        } else if (item.getItemId() == R.id.nav_chart) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_container
                    , new ChartFragment()).commit();
        } else if (item.getItemId() == R.id.nav_chat){
            Intent intent = new Intent(AdminActivity.this, MainChatActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_notify){
            Intent intent = new Intent(AdminActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navAdminListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;
            if (item.getItemId() == R.id.nav_order){
                selected_fragment = new ManageOrderFragment();
            } else if(item.getItemId() == R.id.nav_manage_product){
                selected_fragment = new ManageProductFragment();
            } else if(item.getItemId() == R.id.nav_manage_category){
                selected_fragment = new ManageCategoryFragment();
            }  else if(item.getItemId() == R.id.nav_account){
                selected_fragment = new ManageGoodTomeFragment();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.admin_container, selected_fragment);
            transaction.commit();
            return true;

        }
    };
}