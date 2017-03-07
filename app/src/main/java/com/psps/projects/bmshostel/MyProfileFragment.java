package com.psps.projects.bmshostel;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    signOutListener outListener;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public interface signOutListener{
        public void signOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            outListener = (signOutListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_my_profile, container, false);
        rootView.findViewById(R.id.signOutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outListener.signOut();
            }
        });
        return rootView;
    }

}
