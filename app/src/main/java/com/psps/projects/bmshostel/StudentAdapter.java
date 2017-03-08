package com.psps.projects.bmshostel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ${Shashikant} on 21-02-2017.
 */

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.View_Holder> {

    static class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,branch,year,room;
        ImageView dp;
        View_Holder(View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.nameTv);
            branch=(TextView) itemView.findViewById(R.id.branchTv);
            year=(TextView) itemView.findViewById(R.id.yearTv);
            room=(TextView) itemView.findViewById(R.id.roomTv);
        }

        @Override
        public void onClick(View v) {

        }
    }
    private List<Student> studentList;
    private Context context;

    StudentAdapter(List<Student> studentList, Context context){
        this.studentList=studentList;
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_card,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.name.setText(studentList.get(position).name);
        holder.branch.setText((CharSequence) studentList.get(position).branch);
        holder.year.setText(context.getResources().getQuantityString(R.plurals.semester,0,studentList.get(position).year));
        Log.d("STUDENTADAPTER : ","Year "+studentList.get(position).year);
        holder.room.setText(context.getString(R.string.roomNo,studentList.get(position).roomNo));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
