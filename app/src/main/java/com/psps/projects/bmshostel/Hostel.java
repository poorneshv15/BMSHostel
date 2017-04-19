package com.psps.projects.bmshostel;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Poornesh on 24-03-2017.
 */

public class Hostel extends RealmObject{
    private String hostelName;
    private int floors;
    int roomsPerFloor;
    private String roomsUnderCotrol;
    private int totalRooms;
    private int maxCapacityOfRoom;
    private String currentCapacity;

    public Hostel(){
        //Empty Constructor
    }
    public Hostel(String hostelName) {
        this.hostelName = hostelName;
        switch (hostelName){
            case "ih":
                this.floors=5;
                this.maxCapacityOfRoom=2;
                this.totalRooms=125;
                this.roomsPerFloor=25;
        }
    }

    String getHostelName() {
        return hostelName;
    }

    void setHostelName(String hostelName) {
        switch (hostelName){
            case "ih":
                this.floors=5;
                this.maxCapacityOfRoom=2;
                this.totalRooms=125;
                this.roomsPerFloor=25;
        }
        this.hostelName = hostelName;
    }

    int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getRoomsPerFloor() {
        return roomsPerFloor;
    }

    public void setRoomsPerFloor(int roomsPerFloor) {
        this.roomsPerFloor = roomsPerFloor;
    }

    public int[] getRoomsUnderCotrol() {
        String[] items = this.roomsUnderCotrol.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            };
        }
        return results;
    }

    void setRoomsUnderCotrol(int[] roomsUnderCotrol) {
        this.roomsUnderCotrol=Arrays.toString(roomsUnderCotrol);

    }
    public int[] getCurrentCapacity() {
        String[] items = this.currentCapacity.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
        Log.d("CURRENT CAPACITY(get)",Arrays.toString(items));
        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            };
        }
        return results;
    }

    public void setCurrentCapacity(int[] currentCapacity) {
        Log.d("CURRENT CAPACITY(set)",Arrays.toString(currentCapacity));
        this.currentCapacity=Arrays.toString(currentCapacity);

    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    int getMaxCapacityOfRoom() {
        return maxCapacityOfRoom;
    }

    public void setMaxCapacityOfRoom(int maxCapacityOfRoom) {
        this.maxCapacityOfRoom = maxCapacityOfRoom;
    }
    static Hostel IH=new Hostel("ih");
}
