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
 * Created by Poornesh on 22-02-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {


    List<StudentData> studentDataList;

    public StudentAdapter(List<StudentData> studentDataList) {
        Log.d("STUDENTADAPTER : ","created");
        this.studentDataList = studentDataList;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{

        ImageView studentProfilePic;
        TextView nameTv,roomTv,yearTv;
        public StudentViewHolder(View itemView) {
            super(itemView);
            Log.d("STUDENTVIEWHOLDER : ","creating");
            studentProfilePic=(ImageView)itemView.findViewById(R.id.studentProfileIv);
            nameTv=(TextView)itemView.findViewById(R.id.studentNameTv);
            roomTv=(TextView)itemView.findViewById(R.id.roomNoTv);
            yearTv=(TextView)itemView.findViewById(R.id.studentYearTv);
            Log.d("STUDENTVIEWHOLDER : ","created");
        }
    }
    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_card,parent,false);
        Log.d("ONCREATEVIEWHOLDER : ","created");
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        holder.nameTv.setText(studentDataList.get(position).name);
        holder.yearTv.setText(studentDataList.get(position).year);
        holder.roomTv.setText(studentDataList.get(position).rooomNo);
        holder.studentProfilePic.setImageResource(R.drawable.bms_logo);
        Log.d("ONBINDVIEWHOLDER : ","created");

    }

    @Override
    public int getItemCount() {
        return studentDataList.size();
    }
}
