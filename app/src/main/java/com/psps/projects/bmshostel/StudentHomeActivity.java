package com.psps.projects.bmshostel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentHomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Log.d("STUDENT HOME", " After setContent view");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user==null){
                    startActivity(new Intent(StudentHomeActivity.this,MainActivity.class));
                    finish();
                }
                else{
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
}
