package com.psps.projects.bmshostel;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class AddHosteliteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    RecyclerView studentsRv;
    RoomAdapter roomAdapter;
    GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hostel_rooms_layout);
        /* studentsRv=(RecyclerView)findViewById(R.id.addStudentRv);
        studentsRv.setLayoutManager(new LinearLayoutManager(this));
        studentsRv.setAdapter(new AddHosteliteAdapter(150));*/
        int floors=Hostel.IH.floors;

        SegmentedGroup floorsSg=(SegmentedGroup)findViewById(R.id.floorSg);
        floorsSg.setOnCheckedChangeListener(this);
        floorsSg.setOnClickListener(this);
        for(int i=0;i<floors;i++){
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radio_button_item,null);
            //radioButton.setLayoutParams(params);
            radioButton.setOnClickListener(this);
            radioButton.setId(i);
            radioButton.setText("Button " + (floorsSg.getChildCount() + 1));
            radioButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
            floorsSg.addView(radioButton);
            floorsSg.updateBackground();
        }
        gridview= (GridView) findViewById(R.id.gridview);
        roomAdapter=new RoomAdapter(this,Hostel.IH,0);
        gridview.setAdapter(roomAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int roomNumber=roomAdapter.assignBaseRoom+position;
                Toast.makeText(AddHosteliteActivity.this, "" +roomNumber ,
                        Toast.LENGTH_SHORT).show();
                FragmentManager fm = getSupportFragmentManager();
                AddHosteliteDialogF addHosteliteDialogF = AddHosteliteDialogF.newInstance(roomNumber);
                addHosteliteDialogF.show(fm, "fragment_edit_name");



            }
        });

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Toast.makeText(this, ""+checkedId, Toast.LENGTH_SHORT).show();
        roomAdapter=new RoomAdapter(AddHosteliteActivity.this,Hostel.IH,checkedId);
        gridview.setAdapter(roomAdapter);

    }

    @Override
    public void onClick(View v) {
        RadioButton r=(RadioButton)v;
        r.setChecked(true);
    }
}
