package com.psps.projects.bmshostel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import firebaseclasses.Hostelite;

import java.util.ArrayList;

import io.realm.Realm;


class HosteliteAdapter extends RecyclerView.Adapter<HosteliteAdapter.View_Holder> implements Filterable {





    void setFilter(String queryText) {

        filteredList = new ArrayList<>();
//        constraint = constraint.toString().toLowerCase();
        for (Hostelite item: studentList) {
            if (item.getName().toLowerCase().contains(queryText))
                filteredList.add(item);
        }

        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Hostelite>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("QUERY",constraint.toString());
                ArrayList<Hostelite> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = studentList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    private ArrayList<Hostelite> getFilteredResults(CharSequence constraint) {
        ArrayList<Hostelite> results = new ArrayList<>();
        ArrayList<Hostelite> allStudents= new ArrayList<>(Realm.getDefaultInstance().where(Hostelite.class).findAll());
        for (Hostelite item : allStudents) {
            if (item.name.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,branch,year,room;
        CircleImageView dp;
        ImageButton shortCut;
        View_Holder(View itemView) {
            super(itemView);
            dp=(CircleImageView) itemView.findViewById(R.id.studentProfileIv);
            name =(TextView) itemView.findViewById(R.id.nameTv);
            branch=(TextView) itemView.findViewById(R.id.branchTv);
            year=(TextView) itemView.findViewById(R.id.yearTv);
            room=(TextView) itemView.findViewById(R.id.roomTv);
            shortCut=(ImageButton)itemView.findViewById(R.id.shortCutIv);
            shortCut.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==shortCut.getId()){
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), shortCut);
                //inflating menu from xml resource

                popup.inflate(R.menu.hostelite_short_cut);

                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.call:
                                //handle menu1 click
                                Intent intent=new Intent(Intent.ACTION_CALL);

                                Log.d("HA","Call");
                                break;
                            case R.id.message:
                                //handle menu2 click
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
                return;
            }
            Log.d("HOSTELITE ADAPTER","LayoutPosition "+getLayoutPosition()+"  ItemId"+getItemId()+"  AdapterPosition"+getAdapterPosition());
            Intent hosteliteProfileIntent=new Intent(context,HosteliteProfile.class);
            hosteliteProfileIntent.putExtra("email",filteredList.get(getLayoutPosition()).getEmail());
            context.startActivity(hosteliteProfileIntent);
            //Toast.makeText(v.getContext(), getLayoutPosition(), Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<Hostelite> studentList;
    private ArrayList<Hostelite> filteredList;
    private Context context;

    HosteliteAdapter( Context context){
        this.filteredList= new ArrayList<>(Realm.getDefaultInstance().where(Hostelite.class).findAll());
        this.studentList=filteredList;
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_card,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.name.setText(filteredList.get(position).getName());
        holder.branch.setText( filteredList.get(position).getBranch());
        holder.year.setText(context.getResources().getQuantityString(R.plurals.semester,0,studentList.get(position).getSem()));
        if(filteredList.get(position).getUriPhoto()!=null)
            Glide.with(context).load(filteredList.get(position).getUriPhoto()).into(holder.dp);
        else
            holder.dp.setImageResource(R.drawable.image_dp);
        Log.d("STUDENTADAPTER : ","Year "+filteredList.get(position).getSem());
        holder.room.setText(context.getString(R.string.roomNo,studentList.get(position).getRoomNo()));

    }

    @Override
    public int getItemCount() {
        if(filteredList == null)
            return 0;
        return filteredList.size();
    }


}
