package com.psps.projects.bmshostel;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Arrays;

import firebaseclasses.Hostelite;
import firebaseclasses.HosteliteFirebase;
import io.realm.Realm;

/**
 * Created by Poornesh on 03-04-2017.
 */

public class WardenStartup extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * /@/param name Used to name the worker thread, important only for debugging.
     */
    Hostel hostel;
    String hostelName;
    int[] roomsUnderControl;
    static int[] capacity;
    int j;
    public WardenStartup() {
        super("STARTUP");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final Gson gson=new Gson();
        Log.d("WARDEN STARTUP","Service Started!");
        final FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();


        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Hostelite.class);
                realm.delete(Hostel.class);

            }
        });
        mDatabase.getReference("/wardens/"+intent.getStringExtra("uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("WARDEN STARTUP",dataSnapshot.toString());
                for (DataSnapshot hostelDS:dataSnapshot.getChildren()) {
                    hostelName=hostelDS.getKey();
                    roomsUnderControl=new int[(int) hostelDS.getChildrenCount()];
                    int i=0;
                    for (DataSnapshot rooms:hostelDS.getChildren()){
                        roomsUnderControl[i]= Integer.parseInt(rooms.getKey());
                        Log.d("ROOMS UNDER CONTROL",""+roomsUnderControl[i]);
                        i++;
                    }
                    Log.d("STYARTUP","HostelName: "+hostelName);
                    capacity=getCapacity(roomsUnderControl);
                }
                try(Realm realm=Realm.getDefaultInstance()){

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Hostel hostel=realm.createObject(Hostel.class);
                            hostel.setHostelName(hostelName);
                            hostel.setRoomsUnderCotrol(roomsUnderControl);
                            Log.d("CURRENT CAPACITYsetting",Arrays.toString(capacity));
                            hostel.setCurrentCapacity(capacity);
                            Log.d("ADD HOSTELITE","Rooms"+ Arrays.toString(roomsUnderControl));

                        }
                    });
                }


            }

            private int[] getCapacity(int[] roomsUnderControl) {
                capacity=new int[roomsUnderControl.length];
                Log.d("STARTUP","Get Capacity");
                Log.d("STARTUP","Rooms Length "+roomsUnderControl.length);
                j=0;

                for (int i:roomsUnderControl) {
                    mDatabase.getReference("hostel/"+hostelName+"/"+i+"/hostelites").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            capacity[j]=(int)dataSnapshot.getChildrenCount();
                            Log.d("STARTUP ","Capacity"+dataSnapshot.getChildrenCount()+" -> "+capacity[j]);
                            j++;
                            Log.d("STARTUP","Hostelite UIDs"+dataSnapshot.toString());
                            for (DataSnapshot hostelite:dataSnapshot.getChildren()){
                                getHosteliteDetails(hostelite.getKey());

                            }

                            try(Realm realm=Realm.getDefaultInstance()){

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(Hostel.class).findFirst().setCurrentCapacity(capacity);

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                return capacity;

            }

            private void getHosteliteDetails(String uid) {
                Log.d("STARTUP","Get Hostelite Details"+uid);
                FirebaseDatabase.getInstance().getReference("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        Log.d("STARTUP","Hostelite Details : "+dataSnapshot.getValue());
                        final HosteliteFirebase hosteliteFirebase=dataSnapshot.getValue(HosteliteFirebase.class);
                        Log.d("STARTUP",hosteliteFirebase.toString());

                        try (Realm realm = Realm.getDefaultInstance()) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Hostelite hostelite=realm.createObject(Hostelite.class);
                                    hostelite.setBranch(hosteliteFirebase.branch);
                                    hostelite.setEmail(hosteliteFirebase.email);
                                    hostelite.setName(hosteliteFirebase.name);
                                    hostelite.setMobile(hosteliteFirebase.mobile);
                                    hostelite.setUsn(hosteliteFirebase.usn);
                                    hostelite.setFatherName(hosteliteFirebase.fatherName);
                                    hostelite.setFatherAddress(hosteliteFirebase.fatherAddress);
                                    hostelite.setFatherMobile(hosteliteFirebase.fatherMobile);
                                    hostelite.setGuardianName(hosteliteFirebase.guardianName);
                                    hostelite.setGuardianMobile(hosteliteFirebase.guardianMobile);
                                    hostelite.setGuardianAddress(hosteliteFirebase.guardianAddress);
                                    hostelite.setHostelName(hosteliteFirebase.hostelName);
                                    hostelite.setHostelite(hosteliteFirebase.hostelite);
                                    hostelite.setRoomNo(hosteliteFirebase.roomNo);
                                    hostelite.setSem(hosteliteFirebase.sem);

                                       }


                                    });
                                }
                        catch (Exception e) {
                            Log.d("STARTUP", "REalm Exception : " + e.getMessage());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        stopSelf();
    }
}
