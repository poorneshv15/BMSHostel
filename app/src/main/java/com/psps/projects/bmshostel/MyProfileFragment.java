package com.psps.projects.bmshostel;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;

import firebaseclasses.Hostelite;
import firebaseclasses.Student;
import firebaseclasses.Warden;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    OnMenuClickProfileIF outListener;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    interface OnMenuClickProfileIF {
        void onMenuClickOfProfileFragment(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            outListener = (OnMenuClickProfileIF) context;
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
        LinearLayout linearLayout= (LinearLayout) rootView.findViewById(R.id.fragment_profile_root);
        TextView nameTv= (TextView) rootView.findViewById(R.id.userNameTv);
        nameTv.setText(HomeActivity.user.getDisplayName());
        Uri url=HomeActivity.user.getPhotoUrl();
        Log.d("PROFILE FRAGMENT","DP Uri "+url);
        switch (HomeActivity.userType){
            case "WARDEN":
                Warden warden= Realm.getDefaultInstance().where(Warden.class).contains("email",HomeActivity.user.getEmail()).findFirst();
                addWardenLayout(warden,linearLayout);
                break;
            case "HOSTELITE":
                Hostelite hostelite=Realm.getDefaultInstance().where(Hostelite.class).contains("email",HomeActivity.user.getEmail()).findFirst();
                addHosteliteLayout(hostelite,linearLayout);
                break;
            default:
                Student student=Realm.getDefaultInstance().where(Student.class).contains("email",HomeActivity.user.getEmail()).findFirst();
                addStudentLayout(student,linearLayout);

        }
        ImageView profilePic= (ImageView) rootView.findViewById(R.id.userProfileIv);
        Glide.with(this).load(HomeActivity.user.getPhotoUrl()).into(profilePic);
        new AsyncTask<ImageView,String,String>() {
            @Override
            protected String doInBackground(ImageView... params) {

                return null;
            }
        }.execute(profilePic);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        outListener.onMenuClickOfProfileFragment(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu,menu);
    }

    private void addWardenLayout(Warden warden, LinearLayout linearLayout) {
        LayoutInflater inflater=LayoutInflater.from(linearLayout.getContext());
        View detailsView=inflater.inflate(R.layout.warden_content,null);
        linearLayout.addView(detailsView,2);
        TextView dump= (TextView) detailsView.findViewById(R.id.roomNoTv);
        dump.setText(warden.email);
        dump=(TextView)detailsView.findViewById(R.id.mobileTv);
        dump.setText(warden.mobile);
        dump=(TextView)detailsView.findViewById(R.id.roomsTv);
        Hostel hostel=Realm.getDefaultInstance().where(Hostel.class).findFirst();
        dump.setText(hostel.getHostelName().toUpperCase()+"\n"+ Arrays.toString(hostel.getRoomsUnderCotrol()));

    }

    private void addStudentLayout(Student student,LinearLayout linearLayout) {
        View detailsView=getLayoutInflater(null).inflate(R.layout.student_content,null);
        linearLayout.addView(detailsView,2);
        TextView dump= (TextView) detailsView.findViewById(R.id.roomNoTv);
        dump.setText(HomeActivity.user.getEmail());
        dump=(TextView)detailsView.findViewById(R.id.sUsn);
        dump.setText(student.usn);

    }

    private void addHosteliteLayout(Hostelite hostelite,LinearLayout linearLayout) {
        View detailsView=getLayoutInflater(null).inflate(R.layout.hostelite_content,null);
        linearLayout.addView(detailsView,2);
        TextView dump= (TextView) detailsView.findViewById(R.id.roomNoTv);
        dump.setText(hostelite.getEmail());
        dump=(TextView)detailsView.findViewById(R.id.sRoomNoTv);
        dump.setText(hostelite.getHostelName()+"  "+hostelite.getRoomNo());
        dump=(TextView)detailsView.findViewById(R.id.sUsn);
        dump.setText(hostelite.getUsn());
        dump=(TextView)detailsView.findViewById(R.id.sMobileTv);
        dump.setText(hostelite.getMobile());
        dump=(TextView)detailsView.findViewById(R.id.fNameTv);
        dump.setText(hostelite.getFatherName());
        dump=(TextView)detailsView.findViewById(R.id.fMobileTv);
        dump.setText(hostelite.getFatherMobile());
        dump=(TextView)detailsView.findViewById(R.id.fAddressTv);
        dump.setText(hostelite.getFatherAddress());
        dump=(TextView)detailsView.findViewById(R.id.gNameTv);
        dump.setText(hostelite.getGuardianName());
        dump=(TextView)detailsView.findViewById(R.id.gMobileTv);
        dump.setText(hostelite.getGuardianMobile());
        dump=(TextView)detailsView.findViewById(R.id.gAddressTv);
        dump.setText(hostelite.getGuardianAddress());
    }

}
