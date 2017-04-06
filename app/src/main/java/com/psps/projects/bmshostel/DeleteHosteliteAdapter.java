package com.psps.projects.bmshostel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import firebaseclasses.Hostelite;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by ${Shashikant} on 21-02-2017.
 */

class DeleteHosteliteAdapter extends RecyclerView.Adapter<DeleteHosteliteAdapter.View_Holder> {

    List<String> emails;
    static class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,branch,year,room;
        CheckBox cb;
        ImageView dp;
        View_Holder(View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.nameTv);
            branch=(TextView) itemView.findViewById(R.id.branchTv);
            year=(TextView) itemView.findViewById(R.id.yearTv);
            room=(TextView) itemView.findViewById(R.id.roomTv);
            cb=(CheckBox)itemView.findViewById(R.id.checkBox);
        }

        @Override
        public void onClick(View v) {

        }
    }
    private ArrayList<Hostelite> studentList;
    private Context context;

    DeleteHosteliteAdapter( Context context){

        this.studentList= new ArrayList(Realm.getDefaultInstance().where(Hostelite.class).findAll());
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_del_student,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        holder.name.setText(studentList.get(position).getName());
        holder.branch.setText( studentList.get(position).getBranch());
        holder.year.setText(context.getResources().getQuantityString(R.plurals.semester,0,studentList.get(position).getSem()));
        Log.d("STUDENTADAPTER : ","Year "+studentList.get(position).getSem());
        holder.room.setText(context.getString(R.string.roomNo,studentList.get(position).getRoomNo()));
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)

            {
                if(b)
                {
                    emails.add(studentList.get(position).getEmail());
                }
                else
                {
                    emails.remove(studentList.get(position).getEmail());
                }

            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Item Clicked",Toast.LENGTH_LONG).show();
                holder.cb.setChecked(true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
