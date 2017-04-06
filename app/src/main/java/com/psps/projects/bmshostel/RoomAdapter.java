package com.psps.projects.bmshostel;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

import static com.psps.projects.bmshostel.AddHosteliteActivity.currentCapacity;
import static com.psps.projects.bmshostel.AddHosteliteActivity.maxCapacityPerRoom;
import static com.psps.projects.bmshostel.AddHosteliteActivity.rooms;

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
        ImageView imageView = (ImageView)convertView.findViewById(R.id.roomIv);
        int c=maxCapacityPerRoom;
            int roomNo=assignBaseRoom+position;
            Log.d("ROOM ADAPTER",""+assignBaseRoom+position+rooms.indexOf(roomNo));
        try{
            c=currentCapacity[rooms.indexOf(roomNo)];
        }catch (ArrayIndexOutOfBoundsException e){
            Log.d("EXCEPTION",e.getMessage());
        }


        if(c==0){
            imageView.setImageResource(R.drawable.empty_room);
        }
        else if(c==1){
            imageView.setImageResource(R.drawable.half_empty);
        }
        else{
            imageView.setImageResource(R.drawable.disabled_room);
        }
        // get current item to be displayed
        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.roomTv);
        textViewItemName.setText(String.format(Locale.US,"%d",roomNo));

        return convertView;
    }
}
