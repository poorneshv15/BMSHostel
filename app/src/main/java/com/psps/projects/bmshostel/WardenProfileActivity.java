package com.psps.projects.bmshostel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WardenProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_profile);
        findViewById(R.id.signOutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WardenHomeActivity.mAuth.signOut();
                finish();
            }
        });
    }
}
