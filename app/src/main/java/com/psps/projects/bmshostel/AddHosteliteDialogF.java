package com.psps.projects.bmshostel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ProviderQueryResult;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Poornesh on 25-03-2017.
 */

public class AddHosteliteDialogF extends DialogFragment implements View.OnClickListener{

    List<String> emailsAdded=new ArrayList<>();
    static int roomNumber;
    static boolean accountExists;
    EditText emailEt;
    EditText[] userDetails=new EditText[9];
    TextView roomNoTv;
    ProgressBar loadPb;
    Button addHosteliteButton;
    FetchProviders accountExistsTask;
    ListIterator<Integer> roomsIterator;
    int currentIndex;
    public AddHosteliteDialogF() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    interface AddStudent{
        void addStudent(Bundle bundle);
    }
    static AddStudent addStudentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addStudentListener=(AddStudent)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addStudentListener=null;
    }

    public  static AddHosteliteDialogF newInstance(int roomNo) {
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
        roomNoTv.setText(String.format(Locale.US,"%d",roomsIterator.next()));
        loadPb = (ProgressBar) view.findViewById(R.id.loadPb);
        addHosteliteButton=(Button)view.findViewById(R.id.addHosteliteBtn);
        userDetails[0]=(EditText)view.findViewById(R.id.sNameEt);
        userDetails[1]=(EditText)view.findViewById(R.id.sUsnEt);
        userDetails[2]=(EditText)view.findViewById(R.id.sMobileEt);
        userDetails[3]=(EditText)view.findViewById(R.id.fNameEt);
        userDetails[4]=(EditText)view.findViewById(R.id.fMobileEt);
        userDetails[5]=(EditText)view.findViewById(R.id.fAddressEt);
        userDetails[6]=(EditText)view.findViewById(R.id.gNameEt);
        userDetails[7]=(EditText)view.findViewById(R.id.gMobileEt);
        userDetails[8]=(EditText)view.findViewById(R.id.gAddressEt);
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
                    addHosteliteButton.setText("Invalid Email!!");
                    addHosteliteButton.setEnabled(false);
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
        roomsIterator=AddHosteliteActivity.rooms.listIterator(currentIndex=AddHosteliteActivity.rooms.indexOf(roomNumber));
        Log.d("ADD H F ","Room Number"+roomNumber+" index"+AddHosteliteActivity.rooms.indexOf(roomNumber));
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
        Log.d("ADD DAILOG FRAGMENT","Button CLICKED");
        switch (v.getId()) {
            case R.id.rightArrow:
                if(roomsIterator.hasNext() && AddHosteliteActivity.currentCapacity[roomsIterator.nextIndex()]<AddHosteliteActivity.maxCapacityPerRoom){
                    roomNoTv.setText(String.format(Locale.US, "%d",roomsIterator.next()));
                    currentIndex=roomsIterator.nextIndex();
                    Log.d("ADD DAILOG FRAGMENT","Right"+currentIndex+roomsIterator.hasNext()+roomNoTv.getText().toString());
                }

                break;
            case R.id.leftArrow:
                if(roomsIterator.hasPrevious() && AddHosteliteActivity.currentCapacity[roomsIterator.previousIndex()]<AddHosteliteActivity.maxCapacityPerRoom){
                    roomNoTv.setText(String.format(Locale.US, "%d", roomsIterator.previous()));
                    currentIndex=roomsIterator.previousIndex();
                    Log.d("ADD DAILOG FRAGMENT","Left"+currentIndex+roomsIterator.hasPrevious()+roomNoTv.getText().toString());
                }

                break;
            case R.id.addHosteliteBtn:
                String email=emailEt.getText().toString();
                if(emailsAdded.contains(email)){
                    Toast.makeText(getContext(), "Either Adding or Already added\n plz try After some time", Toast.LENGTH_SHORT).show();
                    return;
                }
                emailsAdded.add(email);
                Log.d("ADDHOSTELITE BUTTON","clicked");
                Bundle bundle=new Bundle();
                bundle.putString("name",userDetails[0].getText().toString());
                bundle.putString("email",email);
                bundle.putString("usn",userDetails[1].getText().toString());
                bundle.putString("mobile",userDetails[2].getText().toString());
                bundle.putString("fName",userDetails[3].getText().toString());
                bundle.putString("fMobile",userDetails[4].getText().toString());
                bundle.putString("fAddress",userDetails[5].getText().toString());
                bundle.putString("gName",userDetails[6].getText().toString());
                bundle.putString("gMobile",userDetails[7].getText().toString());
                bundle.putString("gAddress",userDetails[8].getText().toString());
                bundle.putString("hostel",AddHosteliteActivity.hostelName);
                bundle.putBoolean("accountExists",accountExists);
                bundle.putInt("roomNo",roomNumber);
                addStudentListener.addStudent(bundle);
                break;
            case R.id.roomNoTv:

                break;
            case R.id.loadPb:
                if (accountExists) {
                    loadPb.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.ic_info_outline_black_24dp));

                }
        }
    }

    public void clearAllEditTexts(){
        for(int i=0;i<9;i++){
            userDetails[i].setText(null);
        }
        emailEt.setText(null);
    }

    private class FetchProviders extends AsyncTask<String,Boolean,Boolean>{


        @Override
        protected void onPreExecute() {
            addHosteliteButton.setEnabled(false);
            addHosteliteButton.setText("Please wait...");
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
            accountExists=true;
        }
        else {
            loadPb.setBackgroundColor(Color.YELLOW);
            accountExists=false;
        }
        addHosteliteButton.setEnabled(true);
        addHosteliteButton.setText("ADD");
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



