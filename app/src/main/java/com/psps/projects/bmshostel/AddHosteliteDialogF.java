package com.psps.projects.bmshostel;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by Poornesh on 25-03-2017.
 */

public class AddHosteliteDialogF extends DialogFragment {

    static int roomNumber;
    public AddHosteliteDialogF() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddHosteliteDialogF newInstance(int roomNo) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView roomNoTv;
        // Get field from view
        roomNoTv = (TextView) view.findViewById(R.id.roomNoTv);
        roomNoTv.setText(Integer.toString(AddHosteliteDialogF.roomNumber));
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
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
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

}
