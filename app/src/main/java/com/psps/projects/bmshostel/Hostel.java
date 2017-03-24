package com.psps.projects.bmshostel;

/**
 * Created by Poornesh on 24-03-2017.
 */

public class Hostel {
    String hostelName;
    int floors;
    int roomsPerFloor;
    int totalRooms;
    int maxCapacityOfRoom;
    int currentCapacity[];

    public Hostel(String hostelName, int floors, int roomsPerFloor, int totalRooms, int maxCapacityOfRoom) {
        this.hostelName = hostelName;
        this.floors = floors;
        this.roomsPerFloor = roomsPerFloor;
        this.totalRooms = totalRooms;
        this.maxCapacityOfRoom = maxCapacityOfRoom;
        this.currentCapacity=new int[totalRooms];
        for(int i=0;i<totalRooms;i++)
            this.currentCapacity[i]=0;
        //this.currentCapacity = currentCapacity;
    }
    public static Hostel IH=new Hostel("IH",5,25,125,2);
}
