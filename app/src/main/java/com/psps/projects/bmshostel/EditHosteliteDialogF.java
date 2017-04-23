package com.psps.projects.bmshostel;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import firebaseclasses.Hostelite;
import io.realm.Realm;

import static com.psps.projects.bmshostel.AddHosteliteActivity.rooms;

/**
 * Created by Poornesh on 23-04-2017 for Deleting Hostelites.
 */

public class EditHosteliteDialogF extends DialogFragment {
    EditText[] userDetails=new EditText[9];
    Button editHosteliteButton;
    Spinner sem;
    EditText roomNoEt;
    String[] rooms;
    ArrayList<String> emptyRooms;

    interface EditStudent{
        void editStudent(Bundle bundle);
    }
    EditStudent editStudent;

    public EditHosteliteDialogF() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editStudent=(EditStudent) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        editStudent=null;
    }

    public  static EditHosteliteDialogF newInstance() {
        return new EditHosteliteDialogF();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.edit_hostelite_dialogf, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        TextView email = (TextView) view.findViewById(R.id.sEmailTv);
        roomNoEt=(EditText)view.findViewById(R.id.sRoomNoEt);
        editHosteliteButton=(Button)view.findViewById(R.id.editHosteliteBtn);
        userDetails[0]=(EditText)view.findViewById(R.id.sNameEt);
        userDetails[1]=(EditText)view.findViewById(R.id.sUsnEt);
        userDetails[2]=(EditText)view.findViewById(R.id.sMobileEt);
        userDetails[3]=(EditText)view.findViewById(R.id.fNameTv);
        userDetails[4]=(EditText)view.findViewById(R.id.fMobileTv);
        userDetails[5]=(EditText)view.findViewById(R.id.fAddressEt);
        userDetails[6]=(EditText)view.findViewById(R.id.gNameTv);
        userDetails[7]=(EditText)view.findViewById(R.id.gMobileTv);
        userDetails[8]=(EditText)view.findViewById(R.id.gAddressEt);
        sem=(Spinner)view.findViewById(R.id.sSemEt);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Hostel hostel= Realm.getDefaultInstance().where(Hostel.class).findFirst();
        int[] rooms=hostel.getRoomsUnderCotrol();
        int[] capacity=hostel.getCurrentCapacity();
        int maxCapPerRoom=hostel.getMaxCapacityOfRoom();
        emptyRooms=new ArrayList<>();
        int len=capacity.length;
        for(int i=0;i< len;i++){
            if(capacity[i]<maxCapPerRoom){
                emptyRooms.add(String.valueOf(rooms[i]));
            }
        }
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


}
