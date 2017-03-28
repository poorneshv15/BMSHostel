package com.psps.projects.bmshostel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentHomeActivity extends AppCompatActivity implements MyProfileFragment.signOutListener,StudentListFragment.menuItemClick,DeleteHosteliteFragment.menuItemClickOfDHS {

    public static String USER_TYPE="USER_TYPE";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Fragment fragment;
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("BMSCE");
        Log.d("STUDENT HOME", " After setContent view");
        fragmentManager=getSupportFragmentManager();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nav_view);

        if(getSharedPreferences("user",MODE_PRIVATE).getString(USER_TYPE,"STUDENT").equals("WARDEN")){
            //Add Hostel Fragment
            bottomNavigationView.inflateMenu(R.menu.warden_navigation_menu);
        }
        else{
            bottomNavigationView.inflateMenu(R.menu.student_navigation_menu);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_tt:
                        fragment=new EventsFragment();
                        break;
                    case R.id.nav_syllabus:
                        fragment=new SyllabusFragment();
                        break;
                    case R.id.nav_hostel:
                        fragment=new StudentListFragment();
                        break;
                    case R.id.nav_profile:
                        fragment=new MyProfileFragment();
                        break;
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
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(StudentHomeActivity.this,LoginActivity.class));
                    finish();
                }
                else {
                    try{
                        getSupportActionBar().setTitle(user.getDisplayName());
                    }catch (NullPointerException e){
                        Toast.makeText(StudentHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomNavigationView.setVisibility(View.VISIBLE);
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
        mAuth.signOut();
    }

    @Override
    public void onMenuClick(int id) {
        switch (id){
            case R.id.add_student:
                Intent intent =new Intent(this,AddHosteliteActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_student:
                bottomNavigationView.setVisibility(View.GONE);
                fragment=new DeleteHosteliteFragment();
                final FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.replace(R.id.body_container,fragment).commit();
                ft.addToBackStack(null);
                break;
            case R.id.action_search:




        }
    }

    @Override
    public void onMenuOfDHFClick(int id) {
        switch (id) {

            case R.id.action_search:


        }
    }
}
