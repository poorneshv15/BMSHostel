package com.psps.projects.bmshostel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.psps.projects.bmshostel.realmpackage.Hostelite;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ${Shashikant} on 21-02-2017.
 */

class HosteliteAdapter extends RecyclerView.Adapter<HosteliteAdapter.View_Holder> implements Filterable {
    Realm realm;

    @Override
    public Filter getFilter() {
        return null;
    }

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
    private ArrayList<Hostelite> studentList;
    private Context context;

    HosteliteAdapter( Context context){
        realm=Realm.getDefaultInstance();
        this.studentList= new ArrayList(realm.where(Hostelite.class).findAll());
        this.context=context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_card,parent,false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.name.setText(studentList.get(position).getName());
        holder.branch.setText( studentList.get(position).getBranch());
        holder.year.setText(context.getResources().getQuantityString(R.plurals.semester,0,studentList.get(position).getSem()));
        Log.d("STUDENTADAPTER : ","Year "+studentList.get(position).getSem());
        holder.room.setText(context.getString(R.string.roomNo,studentList.get(position).getRoomNo()));

    }

    @Override
    public int getItemCount() {
        if(studentList == null)
            return 0;
        return studentList.size();
    }
}
