package com.psps.projects.bmshostel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import firebaseclasses.Hostelite;


import java.util.ArrayList;
import java.util.List;

import firebaseclasses.AddHostelitesService;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

public class AddHosteliteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener,AddHosteliteDialogF.AddStudent{

    String TAG="AddHosteliteActivity";
    static List<Integer> rooms;
    RoomAdapter roomAdapter;
    GridView gridview;
    FragmentManager fm;
    static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    static String hostelName;
    static int[] currentCapacity;
    static int maxCapacityPerRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hostel_rooms_layout);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_students);
        setSupportActionBar(toolbar);
        /* studentsRv=(RecyclerView)findViewById(R.id.addStudentRv);
        studentsRv.setLayoutManager(new LinearLayoutManager(this));
        studentsRv.setAdapter(new AddHosteliteAdapter(150));*/
        Hostel hostel=Realm.getDefaultInstance().where(Hostel.class).findFirst();
        maxCapacityPerRoom=hostel.getMaxCapacityOfRoom();
        int floors=hostel.getFloors();
        hostelName=hostel.getHostelName();
        final int[] roomsUnderControl=hostel.getRoomsUnderCotrol();
        rooms=new ArrayList<Integer>() {{ for (int i : roomsUnderControl) add(i); }};
        currentCapacity=hostel.getCurrentCapacity();
        for(int i:roomsUnderControl)
            Log.d("ADD HOSTELITE","Rooms"+i);
        fm = getSupportFragmentManager();
        SegmentedGroup floorsSg=(SegmentedGroup)findViewById(R.id.floorSg);
        floorsSg.setOnCheckedChangeListener(this);
        floorsSg.setOnClickListener(this);
        String[] floorsArray = getResources().getStringArray(R.array.floors);
        for(int i=0;i<floors;i++){
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radio_button_item,null);
            //radioButton.setLayoutParams(params);
            radioButton.setOnClickListener(this);
            radioButton.setId(i);
            radioButton.setText(floorsArray[i]);
            radioButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
            floorsSg.addView(radioButton);
            floorsSg.updateBackground();
        }

        gridview= (GridView) findViewById(R.id.gridview);
        roomAdapter=new RoomAdapter(this,Hostel.IH,0,rooms);
        gridview.setAdapter(roomAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                    // write your action here
                    int roomNumber=roomAdapter.assignBaseRoom+position;
                    Toast.makeText(AddHosteliteActivity.this, "" +roomNumber ,
                            Toast.LENGTH_SHORT).show();
                    if(AddHosteliteActivity.currentCapacity[rooms.indexOf(roomNumber)]==maxCapacityPerRoom)
                        return;
                    AddHosteliteDialogF addHosteliteDialogF = AddHosteliteDialogF.newInstance(roomNumber);
                    addHosteliteDialogF.show(fm, "fragment_edit_name");

            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Toast.makeText(this, ""+checkedId, Toast.LENGTH_SHORT).show();
        roomAdapter=new RoomAdapter(AddHosteliteActivity.this,Hostel.IH,checkedId,rooms);
        gridview.setAdapter(roomAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        RadioButton r=(RadioButton)v;
        r.setChecked(true);
    }

    @Override
    public void addStudent(Bundle bundle) {
        Log.d(TAG,"addStudent");
        currentCapacity[rooms.indexOf(bundle.getInt("roomNo"))]++;
        bundle.putIntArray("currentCapacity",currentCapacity);
        Intent intent=new Intent(this, AddHostelitesService.class);
        intent.putExtras(bundle);
        startService(intent);
        roomAdapter.notifyDataSetChanged();
    }


}
