package com.psps.projects.bmshostel;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import firebaseclasses.DeleteHosteliteService;
import firebaseclasses.Hostelite;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class HosteliteProfileActivity extends AppCompatActivity {


    String email;
    static Hostelite hostelite;
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
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(hostelite.getName()+"'s Profile");
        setSupportActionBar(toolbar);

        ImageView dp=(ImageView)findViewById(R.id.sProfileIv) ;
        if(hostelite.getUriPhoto()!=null)
            Glide.with(this).load(hostelite.getUriPhoto()).into(dp);
        TextView textView=(TextView)findViewById(R.id.nameTv);
        textView.setText(hostelite.getName());
        textView=(TextView)findViewById(R.id.emailTv);
        textView.setText(hostelite.getEmail());
        textView=(TextView)findViewById(R.id.emailTv);
        textView.setText(hostelite.getHostelName().toUpperCase()+" "+hostelite.getRoomNo());
        textView=(TextView)findViewById(R.id.branchSemTv);
        textView.setText(hostelite.getBranch()+"   Sem: "+hostelite.getSem());
        textView=(TextView)findViewById(R.id.sUsn);
        textView.setText(hostelite.getUsn());
        textView=(TextView)findViewById(R.id.sMobileTv);
        textView.setText(hostelite.getMobile());
        textView=(TextView)findViewById(R.id.fNameTv);
        textView.setText(hostelite.getFatherName());
        textView=(TextView)findViewById(R.id.fMobileTv);
        textView.setText(hostelite.getFatherMobile());
        textView=(TextView)findViewById(R.id.fAddressTv);
        textView.setText(hostelite.getFatherAddress());
        textView=(TextView)findViewById(R.id.gNameTv);
        textView.setText(hostelite.getGuardianName());
        textView=(TextView)findViewById(R.id.gMobileTv);
        textView.setText(hostelite.getGuardianMobile());
        textView=(TextView)findViewById(R.id.gAddressTv);
        textView.setText(hostelite.getGuardianAddress());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_student:
                AlertDialog alertDialog=new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setTitle("Confirm Delete?")
                        .setMessage(hostelite.getName()+"\nRoom Number "+hostelite.getRoomNo())
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<String> email=new ArrayList<>(1);
                                email.add(hostelite.getEmail());
                                Intent intent=new Intent(HosteliteProfileActivity.this, DeleteHosteliteService.class);
                                intent.putStringArrayListExtra("emails", email);
                                startService(intent);
                                finish();
                            }
                        })
                        .create();
                alertDialog.show();

                break;
            /*case R.id.edit_details:
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
