package com.psps.projects.bmshostel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Signup_ResetpassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__resetpass);
        Log.d("Signup_ResetpassActivit","onCreate");
    }

    public void signUp(View v){
        Log.d("Signup_ResetpassActivit","signUp");
        startActivity(new Intent(Signup_ResetpassActivity.this,HosteliteSignupActivity.class));
    }
}
