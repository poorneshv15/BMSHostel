package com.psps.projects.bmshostel;


import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by Shashikant on 08-03-2017.
 */

 class AddHosteliteAdapter extends RecyclerView.Adapter<AddHosteliteAdapter.View_Holder>{

    private int no_of_stds;

     AddHosteliteAdapter(int no_of_stds) {
        this.no_of_stds = no_of_stds;
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_hostelite,parent,false);
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
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    boolean validate=false;
                    if(hasFocus){
                        validate=true;
                    }
                    if(validate){
                        Toast.makeText(v.getContext(), "Validating email", Toast.LENGTH_SHORT).show();
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

     }
}
