package firebaseclasses;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psps.projects.bmshostel.AddHosteliteActivity;
import com.psps.projects.bmshostel.Hostel;
import com.psps.projects.bmshostel.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by Poornesh on 29-03-2017.
 */

public class AddHostelitesService extends IntentService {

    String TAG="Add Hostelite service";
    NotificationManager notificationManager;
    Notification myNotification;
    Hostelite hostelite;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public AddHostelitesService() {
        super(AddHostelitesService.class.getName());
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

     DatabaseReference mRef;
     Bundle details;
    boolean accoountExists;
    int roomNo;
    int sem;
     String name ;
     String email;
     String usn;
     String mobile;
     String fName;
     String fMobile;
     String fAddress;
     String gName;
     String gAddress;
     String gMobile;
     String hostel;
    int[] currentCapacity;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        mRef=FirebaseDatabase.getInstance().getReference();
        details=intent.getExtras();
        accoountExists=details.getBoolean("accountExists",false);
        roomNo=details.getInt("roomNo");
        name = details.getString("name");
        email=details.getString("email");
        usn=details.getString("usn");
        mobile=details.getString("mobile");
        fName=details.getString("fName");
        fMobile=details.getString("fMobile");
        fAddress=details.getString("fAddress");
        gName=details.getString("gName");
        gMobile=details.getString("gMobile");
        gAddress=details.getString("gAddress");
        hostel=details.getString("hostel");
        sem=details.getInt("sem");
        currentCapacity=details.getIntArray("currentCapacity");

        //send update
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(Intent.ACTION_DEFAULT);
        intentUpdate.addCategory(Intent.ACTION_SEND);
        sendBroadcast(intentUpdate);

        //generate notification

        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Adding Student")
                .setContentText(name)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .build();

        notificationManager.notify(4, myNotification);


        Log.d(TAG, "Service Started!");


        Log.d(TAG,"HostelPath:"+hostel+"/"+roomNo+"/hostelites");


        if( accoountExists){
            Log.d(TAG,"Account Exists");
            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Log.d("GET DATA", "PARENT: " + childDataSnapshot.getKey());
                        Log.d("GET DATA", "" + childDataSnapshot.child("name").getValue());
                        String uid=childDataSnapshot.getKey();
                        if(childDataSnapshot.hasChild("uriPhoto")){
                            hostelite= Hostelite.create(childDataSnapshot.getKey(),name,email,hostel,roomNo,usn,sem,mobile,fName,fAddress,fMobile,gName,gAddress,gMobile,childDataSnapshot.child("uriPhoto").getValue().toString());
                        }
                        else {
                            hostelite= Hostelite.create(childDataSnapshot.getKey(),name,email,hostel,roomNo,usn,sem,mobile,fName,fAddress,fMobile,gName,gAddress,gMobile, null);
                        }

                        final Map<String,Object> hosteliteValue=hostelite.toMap();
                        HosteliteUid hosteliteUid=new HosteliteUid(uid);
                        Map<String,Object> uidValue=hosteliteUid.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/users/" + uid, hosteliteValue);
                        childUpdates.put("/hostel/" + hostel + "/" + String.format(Locale.US, "%d", roomNo) + "/hostelites/"+uid, uidValue);
                        Log.d("GET DATA", "PARENT: " + childUpdates.toString());
                        //childUpdates.put("/user-posts/" + params[2] + "/" + key, uidValue);
                        Log.d("onHandleIntent", "2" + "UID=" + uid);
                        mRef.updateChildren(childUpdates);
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(hostelite);
                                realm.where(Hostel.class).findFirst().setCurrentCapacity(currentCapacity);
                            }
                        });
                        myNotification = new NotificationCompat.Builder(getApplicationContext())
                                .setContentTitle("Add Hostelite Successful")
                                .setContentText(name+", Room Number-"+roomNo)
                                .setTicker("Notification!")
                                .setWhen(System.currentTimeMillis())
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                                .build();
                        notificationManager.notify(4, myNotification);
                        Log.d("onHandleIntent", "3");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.d("QUERY", "onCancelled ");
                }
            });

        }
        else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user=task.getResult().getUser();
                    String uid=user.getUid();
                    UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(profileChangeRequest);
                    user.sendEmailVerification();
                    Map<String,Object> hosteliteValue=hostelite.toMap();
                    HosteliteUid hosteliteUid=new HosteliteUid(uid);
                    Map<String,Object> uidValue=hosteliteUid.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/"+uid,hosteliteValue);
                    childUpdates.put("/hostel/"+ hostel+"/"+String.format(Locale.US,"%d",roomNo)+"/hostelites/"+uid, uidValue);
                    Log.d("onHandleIntent","2"+"UID="+uid);
                    mRef.updateChildren(childUpdates);
                    Log.d("onHandleIntent","3");

                }
            });



        }
        notificationManager.cancel(7);
        this.stopSelf();
    }

}
