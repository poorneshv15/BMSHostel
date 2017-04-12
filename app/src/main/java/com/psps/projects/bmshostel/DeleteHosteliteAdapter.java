package com.psps.projects.bmshostel;

import android.content.Context;
import android.graphics.Color;
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


    public void flushFilter(){
        filteredList=new ArrayList<>();
        filteredList.addAll(studentList);
        notifyDataSetChanged();
    }

    void setFilter(String queryText) {

        filteredList = new ArrayList<>();
        for (Hostelite item: studentList) {
            if (item.getName().toLowerCase().contains(queryText))
                filteredList.add(item);
        }
        notifyDataSetChanged();
    }


    static List<String> emails=new ArrayList<>();
    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,branch,year,room;
        CheckBox cb;
        ImageView dp;
        View_Holder(View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.nameTv);
            branch=(TextView) itemView.findViewById(R.id.branchTv);
            year=(TextView) itemView.findViewById(R.id.yearTv);
            room=(TextView) itemView.findViewById(R.id.roomTv);
            //cb=(CheckBox)itemView.findViewById(R.id.checkBox);
            /*if(emails.contains(filteredList.get(getAdapterPosition()).getEmail())){
                itemView.setBackgroundColor(Color.rgb(255,0,0));
            }*/
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("DELETE ADAPTER","LayoutPosition "+getLayoutPosition()+"  ItemId"+getItemId()+"  AdapterPosition"+getAdapterPosition());
            if(v.isSelected()){
                Log.d("DHA","Uncheck"+getAdapterPosition());
                emails.remove(studentList.get(getAdapterPosition()).getEmail());
                v.setBackgroundColor(Color.WHITE);
                v.setSelected(false);
            }
            else {
                emails.add(studentList.get(getAdapterPosition()).getEmail());
                Log.d("DHA","check"+getAdapterPosition());
                v.setBackgroundColor(Color.rgb(255,0,0));
                v.setSelected(true);
            }

        }
    }
    private ArrayList<Hostelite> studentList;
    private ArrayList<Hostelite> filteredList;
    private Context context;

    DeleteHosteliteAdapter( Context context){

        this.studentList= new ArrayList(Realm.getDefaultInstance().where(Hostelite.class).findAll());
        this.filteredList=studentList;
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_del_student,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        holder.name.setText(filteredList.get(position).getName());
        holder.branch.setText( filteredList.get(position).getBranch());
        holder.year.setText(context.getResources().getQuantityString(R.plurals.semester,0,filteredList.get(position).getSem()));
        Log.d("STUDENTADAPTER : ","Position "+position);
        holder.room.setText(context.getString(R.string.roomNo,filteredList.get(position).getRoomNo()));
        if(emails.contains(filteredList.get(position).getEmail()))
            holder.itemView.setBackgroundColor(Color.rgb(255,0,0));
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }
}
