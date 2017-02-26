package com.psps.projects.bmshostel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StudentSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StudentSignUpActivity","onCreate1");
        setContentView(R.layout.activity_student_sign_up);
        Log.d("StudentSignUpActivity","onCreate2");
    }
}
