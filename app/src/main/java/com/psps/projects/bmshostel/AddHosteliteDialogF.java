package com.psps.projects.bmshostel;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    FetchProviders accountExistsTask;
    DataSnapshot studentDataSS;
    public AddHosteliteDialogF() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public  static AddHosteliteDialogF newInstance(int roomNo,String hostelName) {
        AddHosteliteDialogF frag = new AddHosteliteDialogF();
        AddHosteliteDialogF.roomNumber=roomNo;
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
        roomNoTv.setText(String.format(Locale.US,"%d",roomNumber));
        loadPb = (ProgressBar) view.findViewById(R.id.loadPb);
        addHosteliteButton=(Button)view.findViewById(R.id.addHosteliteBtn);

        emailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(accountExistsTask !=null)
                    accountExistsTask.cancel(true);

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(AddHosteliteDialogF.isValidEmail(s.toString())){
                    accountExistsTask=new FetchProviders();
                    accountExistsTask.execute(s.toString());

                }
                else {
                    loadPb.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("EditText", "afterTextChanged ");
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



    @NonNull
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
        return ((!enteredEmail.isEmpty())  && (matcher.matches()));
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
                    new AddHosteliteTask().execute(emailEt.getText().toString(), String.format(Locale.US, "%d", roomNumber));
                    return;
                }*/
                Log.d("ADDHOSTELITE BUTTON","clicked");
                addHosteliteButton.setEnabled(false);
                addHosteliteButton.setClickable(false);
                //new AddHosteliteTask().execute(studentDataSS.child("name").toString(),emailEt.getText().toString(), String.format(Locale.US, "%d", roomNumber),studentDataSS.getKey());
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
    private class FetchProviders extends AsyncTask<String,Boolean,Boolean>{


        @Override
        protected void onPreExecute() {
            loadPb.setVisibility(View.VISIBLE);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            loadPb.setVisibility(View.VISIBLE);
            Log.d("RESULT ONPOST","Boolean="+result);


        }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        if(values[0]){
            loadPb.setBackgroundColor(Color.GREEN);
        }
        else {
            loadPb.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
        protected Boolean doInBackground(String... params) {
            final boolean[] accountExists = new boolean[1];
            AddHosteliteActivity.mAuth.fetchProvidersForEmail(params[0]).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    if(task.isSuccessful()){
                        ///////// getProviders() will return size 1. if email ID is available.
                        try{
                            if(task.getResult().getProviders().size()!=0){
                                accountExists[0] =true;
                                publishProgress(true);

                                //loadPb.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_info_outline_black_24dp));

                            }
                            else {
                                accountExists[0]=false;
                                publishProgress(false);
                                //loadPb.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_info_outline_black_24dp));

                            }
                        }
                        catch(NullPointerException e){
                            Log.d("ADD HOSTELITE F","Exception "+e.getMessage());
                        }
                    }
                }
            });

            return accountExists[0];
        }
    }

}



