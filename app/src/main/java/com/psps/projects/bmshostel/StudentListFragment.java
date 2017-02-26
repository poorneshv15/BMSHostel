package com.psps.projects.bmshostel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends Fragment {

    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    List<Student> studentList;

    public StudentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_student_list, container, false);
        studentList=new ArrayList<>();
        studentList.add(new Student("Rajath","CSE",6,2));
        studentList.add(new Student("is","ECE",4,32));
        studentList.add(new Student("Handsome","BT",3,2));
        studentList.add(new Student("Boy","EEE",4,3));
        studentAdapter=new StudentAdapter(studentList,rootView.getContext());
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentAdapter);
        return rootView;
    }

}
