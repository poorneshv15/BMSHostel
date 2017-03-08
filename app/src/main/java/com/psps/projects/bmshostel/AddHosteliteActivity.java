package com.psps.projects.bmshostel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AddHosteliteActivity extends AppCompatActivity {

    RecyclerView studentsRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hostelite);
        studentsRv=(RecyclerView)findViewById(R.id.addStudentRv);
        studentsRv.setLayoutManager(new LinearLayoutManager(this));
        studentsRv.setAdapter(new AddHosteliteAdapter(150));

    }

}
