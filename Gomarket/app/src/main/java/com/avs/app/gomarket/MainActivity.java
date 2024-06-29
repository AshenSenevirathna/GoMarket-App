package com.avs.app.gomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.avs.app.gomarket.models.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,homeFragment);
        fragmentTransaction.commit();
        drawerLayout.close();

        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.nav_header_name);
        TextView headerEmail = headerView.findViewById(R.id.nav_header_email);
        CircleImageView headerImg = headerView.findViewById(R.id.nav_header_img);

        DatabaseReference databaseReference = database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid());
        databaseReference.child("profileImg")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String profileUrl = snapshot.getValue(String.class);
                            loadingProfilePicture(profileUrl);
                        }else {
                            headerImg.setImageResource(R.drawable.user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        headerName.setText(userModel.getUserFname()+""+userModel.getUserLname());
                        headerEmail.setText(userModel.getUserEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//
//                        headerName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
//                        headerEmail.setText(userModel.getUserEmail());
//                        Glide.with(MainActivity.this).load(userModel.getProfileImg()).into(headerImg);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

    }

    private void loadingProfilePicture(String profileUrl) {

        View headerView = navigationView.getHeaderView(0);
        CircleImageView headerImg = headerView.findViewById(R.id.nav_header_img);

        Glide.with(getApplicationContext())
                .load(profileUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(headerImg);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        if (item.getItemId() == R.id.sideNavHome){
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,homeFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavProfile){
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,profileFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavCategory){
            CategoryFragment categoryFragment = new CategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,categoryFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavNewProduct){
            NewProductFragment newProductFragment = new NewProductFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,newProductFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavMyOrders){
            MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,myOrdersFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavCart){
            CartFragment cartFragment = new CartFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,cartFragment);
            fragmentTransaction.commit();
            drawerLayout.close();
        }
        if (item.getItemId() == R.id.sideNavLogout){
            String currentUser = auth.getCurrentUser().getUid();
            if(currentUser!= null){
                FirebaseAuth.getInstance().signOut();
                AlertDialog .Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert!")
                        .setMessage("Do you want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                drawerLayout.close();
                            }
                        });
                builder.show();

            }
        }

        //bottom navigation
        if (item.getItemId() == R.id.bottomNavHome){
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,homeFragment);
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.bottomNavCategory){
            CategoryFragment categoryFragment = new CategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,categoryFragment);
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.bottomNavCart){
            CartFragment cartFragment = new CartFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,cartFragment);
            fragmentTransaction.commit();
        }
        if (item.getItemId()== R.id.bottomNavProfile){
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,profileFragment);
            fragmentTransaction.commit();
        }
        return true;


    }

}