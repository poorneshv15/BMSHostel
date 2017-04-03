package com.psps.projects.bmshostel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

/**
 * Created by Poornesh on 22-03-2017.
 */

class RoomAdapter extends BaseAdapter {

    private Context mContext;
    private Hostel hostel;
    int assignBaseRoom;
    private List<Integer> roomsUnderControl;
    RoomAdapter(Context c, Hostel hostel,int currentFloor,List<Integer> roomsUnderControl) {
        mContext = c;
        this.hostel = hostel;
        assignBaseRoom=currentFloor*hostel.roomsPerFloor+1;
        this.roomsUnderControl=roomsUnderControl;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // Return true for clickable, false for not
        return roomsUnderControl.contains(assignBaseRoom+position);
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
        TextView textView;
        textView=new TextView(mContext);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.room_with_label, parent, false);
            textView.setLayoutParams(new GridView.LayoutParams(100, 200));
            //view.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //imageView.setPadding(8, 8, 8, 8);
            //textView.setLabelFor(imageView.getId());
            //textView.setLayoutParams(new GridView.LayoutParams(100,20));
        }
        if(isEnabled(position)){
            ImageView imageView = (ImageView)convertView.findViewById(R.id.roomIv);
            imageView.setBackgroundColor(Color.BLUE);
        }

        // get current item to be displayed
        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.roomTv);
        textViewItemName.setText(String.format(Locale.US,"%d",assignBaseRoom+position));

        return convertView;
    }
}
