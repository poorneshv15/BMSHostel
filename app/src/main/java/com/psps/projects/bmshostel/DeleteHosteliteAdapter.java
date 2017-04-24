package com.psps.projects.bmshostel;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
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
        CircleImageView dp;
        View_Holder(View itemView) {
            super(itemView);
            dp=(CircleImageView)itemView.findViewById(R.id.studentProfileIv);
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
            String email=filteredList.get(getAdapterPosition()).getEmail();
            Log.d("DELETE ADAPTER","LayoutPosition "+getLayoutPosition()+"  ItemId"+getItemId()+"  AdapterPosition"+getAdapterPosition()+" email"+email);
            if(emails.contains(email)){
                Log.d("DHA","Uncheck"+getLayoutPosition());
                emails.remove(email);
                v.setBackgroundColor(Color.WHITE);
                v.setSelected(false);
            }
            else {
                emails.add(email);
                Log.d("DHA","check"+getLayoutPosition());
                v.setBackgroundColor(Color.argb(206,241,137,137));
                v.setSelected(true);
            }

        }
    }
    private ArrayList<Hostelite> studentList;
    private ArrayList<Hostelite> filteredList;
    private Context context;

    DeleteHosteliteAdapter( Context context){

        this.studentList= new ArrayList<>(Realm.getDefaultInstance().where(Hostelite.class).findAll());
        this.filteredList=studentList;
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("DHA","onCreteViewHolder");
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
            holder.itemView.setBackgroundColor(Color.argb(206,241,137,137));
        else
            holder.itemView.setBackgroundColor(Color.WHITE);
        if(filteredList.get(position).getUriPhoto()!=null)
            Glide.with(context).load(filteredList.get(position).getUriPhoto()).into(holder.dp);
        else
            holder.dp.setImageResource(R.drawable.image_dp);

    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }
}
