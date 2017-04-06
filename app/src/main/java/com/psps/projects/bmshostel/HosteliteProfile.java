package com.psps.projects.bmshostel;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import firebaseclasses.Hostelite;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class HosteliteProfile extends AppCompatActivity {


    String email;
    Hostelite hostelite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hostelite_profile);
        email=getIntent().getStringExtra("email");
        try(Realm realm=Realm.getDefaultInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    hostelite=realm.where(Hostelite.class).equalTo("email",email).findFirst();
                }
            });
        }
        setContentView(R.layout.hostelite_profile);
        TextView textView=(TextView)findViewById(R.id.nameTv);
        textView.setText(hostelite.getName());
        textView=(TextView)findViewById(R.id.sRoomNoTv);
        textView.setText(""+hostelite.getRoomNo());
        textView=(TextView)findViewById(R.id.branchSemTv);
        textView.setText(hostelite.getBranch()+"   Sem: "+hostelite.getSem());
        textView=(TextView)findViewById(R.id.sUsn);
        textView.setText(hostelite.getUsn());
        textView=(TextView)findViewById(R.id.sMobileTv);
        textView.setText(""+hostelite.getMobile());
        textView=(TextView)findViewById(R.id.fNameEt);
        textView.setText(hostelite.getFatherName());
        textView=(TextView)findViewById(R.id.fMobileEt);
        textView.setText(""+hostelite.getFatherMobile());
        textView=(TextView)findViewById(R.id.fAddressTv);
        textView.setText(hostelite.getFatherAddress());
        textView=(TextView)findViewById(R.id.gNameEt);
        textView.setText(hostelite.getGuardianName());
        textView=(TextView)findViewById(R.id.gMobileEt);
        textView.setText(""+hostelite.getGuardianMobile());
        textView=(TextView)findViewById(R.id.gAddressTv);
        textView.setText(hostelite.getGuardianAddress());
    }

}
