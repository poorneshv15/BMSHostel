package com.psps.projects.bmshostel;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import firebaseclasses.DeleteHosteliteService;


public class HomeActivity extends AppCompatActivity implements MyProfileFragment.OnMenuClickProfileIF,StudentListFragment.menuItemClick,DeleteHosteliteFragment.menuItemClickOfDHS {

    private static final int MY_PERMISSIONS_REQUEST_CALL = 2;
    static boolean searchExpanded=false;
    public static String USER_TYPE="USER_TYPE";
    public static String userType;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    static FirebaseUser user;
    Fragment fragment;
    int lastFragment;
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        lastFragment=R.id.nav_hostel;
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    finish();
                }
                else {
                    try{
                        toolbar.setTitle(user.getDisplayName());
                    }catch (NullPointerException e){
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };
        setSupportActionBar(toolbar);
        toolbar.setTitle("BMSCE");
        Log.d("STUDENT HOME", " After setContent view");
        fragmentManager=getSupportFragmentManager();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nav_view);
        userType=getSharedPreferences("user",MODE_PRIVATE).getString(USER_TYPE,"STUDENT");
        switch (userType) {
            case "WARDEN": {
                //Add Hostel Fragment
                bottomNavigationView.inflateMenu(R.menu.warden_navigation_menu);
                break;
            }
            case "HOSTELITE":
                bottomNavigationView.inflateMenu(R.menu.student_navigation_menu);
                break;
            default: {
                bottomNavigationView.inflateMenu(R.menu.student_navigation_menu);
                break;
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_tt:
                        if(lastFragment==R.id.nav_tt)
                            return false;
                        lastFragment=R.id.nav_tt;
                        fragment=new EventsFragment();
                        break;
                    /*case R.id.nav_syllabus:
                        fragment=new SyllabusFragment();
                        break;*/
                    case R.id.nav_hostel:
                        if(lastFragment==R.id.nav_hostel)
                            return false;
                        lastFragment=R.id.nav_hostel;
                        fragment=new StudentListFragment();
                        break;
                    case R.id.nav_profile:
                        if(lastFragment==R.id.nav_profile)
                            return false;
                        lastFragment=R.id.nav_profile;
                        fragment=new MyProfileFragment();
                        break;
                }
                final FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.replace(R.id.body_container,fragment).commit();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CALL_PHONE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                    }
                });
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(searchExpanded)
            toolbar.collapseActionView();
        else
            super.onBackPressed();
        bottomNavigationView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
        final FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.replace(R.id.body_container,new StudentListFragment()).commit();
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
    public void onMenuClick(int id) {
        switch (id){
            case R.id.add_student:
                int[] roomNumbers={1,2,3,4,5,6,7,8,9};
                Intent intent =new Intent(this,AddHosteliteActivity.class);
                intent.putExtra("roomNumbers",roomNumbers);
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
                Log.d("HOME","OnMEnu Click ACTION SEARCH");



        }
    }

    @Override
    public void onMenuOfDHFClick(int id) {
        switch (id) {

            case R.id.action_search:
                Log.d("HOME","OnMEnu Click ACTION SEARCH");
                break;
            case R.id.action_done:
                //Delete Selected Students...............
                Intent intent=new Intent(this, DeleteHosteliteService.class);
                intent.putStringArrayListExtra("emails", (ArrayList<String>) DeleteHosteliteAdapter.emails);
                startService(intent);
                DeleteHosteliteAdapter.emails.clear();
                fragmentManager.popBackStack();
                bottomNavigationView.setVisibility(View.VISIBLE);
                break;



        }
    }

    @Override
    public void onMenuClickOfProfileFragment(int id) {
        switch (id){
            case R.id.sign_out:
                ProgressDialog progress=new ProgressDialog(this);
                progress.setMessage("Signing Out...");
                progress.show();
                mAuth.signOut();
                break;
            /*case R.id.edit_details:
                Toast.makeText(this, "Edit Profile\nunder Contruction", Toast.LENGTH_SHORT).show();
                break;*/
        }
    }
}
