package com.psps.projects.bmshostel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends Fragment {


    StudentAdapter studentAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public StudentListFragment()  {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("STUDENTLISTFRAGMENT : ","onCreateView");
        recyclerView=(RecyclerView)getView().findViewById(R.id.studentDataRv);
        linearLayoutManager=new LinearLayoutManager(getContext());
        List<StudentData> dummyData= new ArrayList<>();
        dummyData.add(new StudentData("Pratik",2,1));
        dummyData.add(new StudentData("Poornesh",4,5));
        dummyData.add(new StudentData("shashikant",6,8));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentAdapter);
        Log.d("STUDENTLISTFRAGMENT : ","oncreatedView");
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

}
