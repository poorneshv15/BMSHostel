package com.psps.projects.bmshostel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poornesh on 22-03-2017.
 */

class RoomAdapter extends BaseAdapter {

    private Context mContext;
    private Hostel hostel;
    int currentFloor;
    int assignBaseRoom;
    public RoomAdapter(Context c, Hostel hostel,int currentFloor) {
        mContext = c;
        this.hostel = hostel;
        this.currentFloor=currentFloor;
        assignBaseRoom=currentFloor*hostel.roomsPerFloor+1;
    }

    @Override
    public int getCount() {
        return hostel.roomsPerFloor;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        View view;
        textView=new TextView(mContext);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.room_with_label, parent, false);
            imageView = new ImageView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(100, 200));
            //view.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //imageView.setPadding(8, 8, 8, 8);
            //textView.setLabelFor(imageView.getId());
            //textView.setLayoutParams(new GridView.LayoutParams(100,20));
        }

        // get current item to be displayed
        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.roomTv);
        textViewItemName.setText(Integer.toString(assignBaseRoom+position));

        return convertView;
    }
}
