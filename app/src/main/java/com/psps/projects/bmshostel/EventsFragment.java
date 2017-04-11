package com.psps.projects.bmshostel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment{


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_events, container, false);

        ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(getContext());
        mViewPager.setAdapter(adapterView);
        return rootView;
    }

}
