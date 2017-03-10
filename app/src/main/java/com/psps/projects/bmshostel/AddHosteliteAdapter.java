package com.psps.projects.bmshostel;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ProviderQueryResult;

import net.cachapa.expandablelayout.ExpandableLayout;

import static com.psps.projects.bmshostel.WardenHomeActivity.mAuth;

/**
 * Created by Shashikant on 08-03-2017.
 */

 class AddHosteliteAdapter extends RecyclerView.Adapter<AddHosteliteAdapter.View_Holder>{

    private int no_of_stds;
    private Handler handler=new Handler();
    Context context;
     AddHosteliteAdapter(int no_of_stds) {
        this.no_of_stds = no_of_stds;
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v=LayoutInflater.from(context).inflate(R.layout.add_hostelite,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        if(position%2==0)
            holder.rootView.setBackgroundColor(Color.rgb(171,198,230));
        else
            holder.rootView.setBackgroundColor(Color.TRANSPARENT);
        holder.slNoTv.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return no_of_stds;
    }

     class View_Holder extends RecyclerView.ViewHolder{
        EditText s_email,s_room_no;//s_usn,s_mobile_no,f_name,f_mobile,f_address,g_name,g_mobile,g_address;
         TextView expand,slNoTv;
         ExpandableLayout expandableLayout;
         View rootView;

         View_Holder(View itemView) {
            super(itemView);
            s_email=(EditText)itemView.findViewById(R.id.sEmailEt);
            s_room_no=(EditText)itemView.findViewById(R.id.sRoomNoEt);
            /*s_usn=(EditText)itemView.findViewById(R.id.sUsnEt);
            s_mobile_no=(EditText)itemView.findViewById(R.id.sMobileEt);
            f_name=(EditText)itemView.findViewById(R.id.fNameEt);
            f_mobile=(EditText)itemView.findViewById(R.id.fMobileEt);
            f_address=(EditText)itemView.findViewById(R.id.fAddressEt);
            g_name=(EditText)itemView.findViewById(R.id.gNameEt);
            g_mobile=(EditText)itemView.findViewById(R.id.gMobileEt);
            g_address=(EditText)itemView.findViewById(R.id.gAddressEt);*/
            expand=(TextView)itemView.findViewById(R.id.expandBtn);
            slNoTv=(TextView)itemView.findViewById(R.id.slNoTv);
            expandableLayout=(ExpandableLayout)itemView.findViewById(R.id.expandLayout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            rootView=itemView.findViewById(R.id.addStudentRoot);

            s_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                boolean validate=false;
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(validate){
                        Toast.makeText(v.getContext(), "Validating email", Toast.LENGTH_SHORT).show();
                        validate=false;
                        if(!s_email.getText().toString().equals(""))
                            new Thread(new Validation()).start();
                    }
                    if(hasFocus){
                        validate=true;
                    }

                }
            });
            s_room_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                boolean validate=false;
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(validate){
                        Toast.makeText(v.getContext(), "Validating room", Toast.LENGTH_SHORT).show();
                        validate=false;
                    }
                    if(hasFocus){
                        validate=true;
                    }

                }
            });
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
        }
         private class Validation implements Runnable {
             @Override
             public void run() {
                 mAuth.fetchProvidersForEmail(s_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                     @Override
                     public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                         if(task.isSuccessful()){
                             ///////// getProviders() will return size 1. if email ID is available.
                             try{
                                 if(task.getResult().getProviders().size()!=0){
                                     Toast.makeText(context, "ACCOUNT EXISTS ON EMAIL ", Toast.LENGTH_SHORT).show();
                                 }
                             }
                             catch(NullPointerException e){
                                 Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                             }
                         }
                     }
                 });
             }
         }

     }


}
