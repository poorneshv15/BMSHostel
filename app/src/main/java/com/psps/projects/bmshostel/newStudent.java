package com.psps.projects.bmshostel;

/**
 * Created by patil on 03-03-2017.
 */

public class newStudent {
    String usn;
    boolean hostelite;

    public newStudent() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public newStudent( String usn) {
        this.usn = usn;
        this.hostelite=false;
    }
}