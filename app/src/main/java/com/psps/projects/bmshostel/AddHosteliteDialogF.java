package com.psps.projects.bmshostel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import FirebaseClasses.Hostelite;
import FirebaseClasses.HosteliteUid;

/**
 * Created by Poornesh on 25-03-2017.
 */

public class AddHosteliteDialogF extends DialogFragment implements View.OnClickListener{

    static int roomNumber;
    static boolean accountExists;
    EditText emailEt;
    TextView roomNoTv;
    ProgressBar loadPb;
    Button addHosteliteButton;
    static String hostelName;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DataSnapshot studentDataSS;
    public AddHosteliteDialogF() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public  static AddHosteliteDialogF newInstance(int roomNo,String hostelName) {
        AddHosteliteDialogF frag = new AddHosteliteDialogF();
        AddHosteliteDialogF.roomNumber=roomNo;
        AddHosteliteDialogF.hostelName=hostelName;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_hostelite_dialogf, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEt=(EditText)view.findViewById(R.id.sEmailEt);
        // Get field from view
        roomNoTv = (TextView) view.findViewById(R.id.roomNoTv);
        roomNoTv.setText(Integer.toString(roomNumber));
        ToggleButton expand=(ToggleButton)view.findViewById(R.id.expandBtn);
        final ExpandableLayout expandableLayout=(ExpandableLayout)view.findViewById(R.id.expandLayout);
        expand.setOnClickListener(new View.OnClickListener() {
            boolean expand=false;
            @Override
            public void onClick(View v) {

                if(!expand){
                    expand=true;
                    expandableLayout.expand();
                }
                else{
                    expand=false;
                    expandableLayout.collapse();
                }
            }
        });
        loadPb = (ProgressBar) view.findViewById(R.id.loadPb);
        addHosteliteButton=(Button)view.findViewById(R.id.addHosteliteBtn);

        emailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(AddHosteliteDialogF.isValidEmail(s.toString())){
                    loadPb.setVisibility(View.VISIBLE);
                    mAuth.fetchProvidersForEmail(s.toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            loadPb.setVisibility(View.INVISIBLE);
                            if(task.isSuccessful()){
                                ///////// getProviders() will return size 1. if email ID is available.
                                try{
                                    if(task.getResult().getProviders().size()!=0){
                                        accountExists=true;
                                        loadPb.setVisibility(View.VISIBLE);
                                        loadPb.clearAnimation();
                                        loadPb.setProgress(0);

                                        loadPb.setBackgroundColor(Color.GREEN);
                                        if(studentDataSS == null){
                                            getStudentData(s);
                                            Log.d("EditText", "getting student data... ");
                                        }



                                        //loadPb.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_info_outline_black_24dp));

                                    }
                                    else {
                                        if(addHosteliteButton.isEnabled())
                                            addHosteliteButton.setEnabled(false);
                                        studentDataSS=null;
                                        Log.d("EditText", "addHosteliteButton false ");
                                        accountExists=false;
                                        loadPb.setVisibility(View.VISIBLE);
                                        loadPb.setBackgroundColor(Color.YELLOW);
                                        //loadPb.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_info_outline_black_24dp));

                                    }
                                }
                                catch(NullPointerException e){
                                    Log.d("ADD HOSTELITE F","Exception "+e.getMessage());
                                }
                            }
                        }
                    });
                }
                else {
                    loadPb.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        roomNoTv.setOnClickListener(this);
        loadPb.setOnClickListener(this);
        view.findViewById(R.id.rightArrow).setOnClickListener(this);
        view.findViewById(R.id.leftArrow).setOnClickListener(this);
        addHosteliteButton.setOnClickListener(this);


        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        /*roomNoTv.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
    }


    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x ), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static boolean isValidEmail(String enteredEmail){
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (enteredEmail!=null) && (matcher.matches()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightArrow:
                roomNumber += 1;
                roomNoTv.setText(String.format(Locale.US, "%d", roomNumber));
                break;
            case R.id.leftArrow:
                roomNumber -= 1;
                roomNoTv.setText(String.format(Locale.US, "%d", roomNumber));
                break;
            case R.id.addHosteliteBtn:
                /*if(studentDataSS==null){
                    new addHostelite().execute(emailEt.getText().toString(), String.format(Locale.US, "%d", roomNumber));
                    return;
                }*/
                Log.d("ADDHOSTELITE BUTTON","clicked");
                addHosteliteButton.setEnabled(false);
                addHosteliteButton.setClickable(false);
                new addHostelite().execute(studentDataSS.child("name").toString(),emailEt.getText().toString(), String.format(Locale.US, "%d", roomNumber),studentDataSS.getKey());
                break;
            case R.id.roomNoTv:

                break;
            case R.id.loadPb:
                if (accountExists) {
                    loadPb.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.ic_info_outline_black_24dp));

                }
        }
    }
    public void getStudentData(CharSequence email){

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d("QUERY", "onDataChange "+dataSnapshot);
                for(DataSnapshot userSS:dataSnapshot.getChildren())
                    studentDataSS =userSS;
               // messageHandler.sendEmptyMessage(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("QUERY", "onCancelled ");
            }
        };
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email.toString());
        query.addListenerForSingleValueEvent(valueEventListener);
        Log.d("QUERY", "end ");/*

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d("GET DATA", "PARENT: " + childDataSnapshot.getKey());
                    Log.d("GET DATA", "" + childDataSnapshot.child("name").getValue());
                    studentDataSS = childDataSnapshot;
                    //messageHandler.sendEmptyMessage(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("QUERY", "onCancelled ");
            }
        });*/
    }
/*
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            addHosteliteButton.setEnabled(true);
            Log.d("Handler", "addHosteliteButton true ");
        }
    };
*/

}


     class addHostelite extends AsyncTask<String,Integer,String>{


        @Override
        protected String doInBackground(String... params) {
            FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
            DatabaseReference mRef=mDatabase.getReference();
            final DataSnapshot[] studentDataSS = new DataSnapshot[1];

            if(AddHosteliteDialogF.accountExists){
                Log.d("AsyncTask","1");
                //mRef=mDatabase.getReference("hostel/"+AddHosteliteDialogF.hostelName+"/"+params[1]+"/hostelites");
                //String key = mRef.child("hostel").child(AddHosteliteDialogF.hostelName).child(params[1]).child("hostelites").toString();
                String key = mRef.child("posts").push().getKey();
                Hostelite hostelite=new Hostelite(params[0],params[1],AddHosteliteDialogF.hostelName,AddHosteliteDialogF.roomNumber);
                Map<String,Object> hosteliteValue=hostelite.toMap();
                HosteliteUid hosteliteUid=new HosteliteUid(params[3]);
                Map<String,Object> uidValue=hosteliteUid.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/"+params[3],hosteliteValue);
                childUpdates.put("/hostel/"+AddHosteliteDialogF.hostelName+"/"+params[1]+"/hostelites", uidValue);
                //childUpdates.put("/user-posts/" + params[2] + "/" + key, uidValue);
                Log.d("AsyncTask","2"+key+"UID="+params[2]);
                mRef.updateChildren(childUpdates);
                Log.d("AsyncTask","3");
            }
            AddHosteliteActivity.mAuth.createUserWithEmailAndPassword(params[0],params[0]);
            return null;
        }
    }