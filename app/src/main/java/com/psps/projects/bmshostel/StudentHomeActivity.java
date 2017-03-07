package com.psps.projects.bmshostel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentHomeActivity extends AppCompatActivity implements MyProfileFragment.signOutListener{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Fragment fragment;
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
        if(!preferences.getBoolean("student",false)){
            startActivity(new Intent(StudentHomeActivity.this,WardenHomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_student_home);
        Log.d("STUDENT HOME", " After setContent view");
        fragmentManager=getSupportFragmentManager();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        fragment=new MyProfileFragment();
                        break;
                    case R.id.nav_tt:
                        fragment=new EventsFragment();
                        break;
                    case R.id.nav_syllabus:
                        fragment=new SyllabusFragment();
                }
                final FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.replace(R.id.body_container,fragment).commit();
                return true;
            }
        });
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user==null){
                    startActivity(new Intent(StudentHomeActivity.this,MainActivity.class));
                    finish();
                }
                else {
                    getSupportActionBar().setTitle(user.getDisplayName());
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void signOut() {
        Log.d("STUDENT HOME : ","SIGNOUT");
        user=null;
        mAuth.signOut();
    }
}
