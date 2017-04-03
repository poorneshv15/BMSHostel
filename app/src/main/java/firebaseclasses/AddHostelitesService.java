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
import com.psps.projects.bmshostel.R;
import com.psps.projects.bmshostel.realmpackage.Hostelite;

import java.io.Serializable;
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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        //send update
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(Intent.ACTION_DEFAULT);
        intentUpdate.addCategory(Intent.ACTION_SEND);
        sendBroadcast(intentUpdate);

        //generate notification
        String notificationText = "Adding Student";
        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Progress")
                .setContentText(notificationText)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .build();

        notificationManager.notify(7, myNotification);


        Log.d(TAG, "Service Started!");
        FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference mRef=mDatabase.getReference();
        final Bundle details=intent.getExtras();
        boolean accoountExists=details.getBoolean("accountExists",false);
        final int roomNo=details.getInt("roomNo");
        final String name = intent.getStringExtra("name");
        final String email=intent.getStringExtra("email");
        final String usn=intent.getStringExtra("usn");
        final String mobile=intent.getStringExtra("mobile");
        final String fName=intent.getStringExtra("fName");
        final String fMobile=intent.getStringExtra("fMobile");
        final String fAddress=intent.getStringExtra("fAddress");
        final String gName=intent.getStringExtra("gName");
        final String gAddress=intent.getStringExtra("gAddress");
        final String gMobile=intent.getStringExtra("gMobile");
        final String hostel=intent.getStringExtra("hostel");
        Log.d(TAG,"HostelPath:"+hostel+"/"+roomNo+"/hostelites");
        hostelite= Hostelite.create(name,email,hostel,roomNo,usn,mobile,fName,fAddress,fMobile,gName,gAddress,gMobile);

        if( accoountExists){
            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Log.d("GET DATA", "PARENT: " + childDataSnapshot.getKey());
                        Log.d("GET DATA", "" + childDataSnapshot.child("name").getValue());
                        String uid=childDataSnapshot.getKey();

                        Map<String,Object> hosteliteValue=hostelite.toMap();
                        HosteliteUid hosteliteUid=new HosteliteUid(uid);
                        Map<String,Object> uidValue=hosteliteUid.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        String hostelites="hostelites";
                        childUpdates.put("/users/"+uid,hosteliteValue);
                        childUpdates.put("/hostel/"+ hostel+"/"+String.format(Locale.US,"%d",roomNo)+"/"+hostelites, uidValue);
                        Log.d("GET DATA", "PARENT: " +childUpdates.toString());
                        //childUpdates.put("/user-posts/" + params[2] + "/" + key, uidValue);
                        Log.d("onHandleIntent","2"+"UID="+uid);
                        mRef.updateChildren(childUpdates);
                        sendBroadCast(details);
                        Log.d("onHandleIntent","3");
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
                    childUpdates.put("/hostel/"+ hostel+"/"+String.format(Locale.US,"%d",roomNo)+"/hostelites", uidValue);
                    Log.d("onHandleIntent","2"+"UID="+uid);
                    mRef.updateChildren(childUpdates);
                    sendBroadCast(details);
                    Log.d("onHandleIntent","3");
                }
            });

            notificationManager.cancel(7);
        }



        Log.d(TAG, "Service Stopping!");
        this.stopSelf();


    }

    private void sendBroadCast(Bundle details){
        //ADD TO LOCAL DATABASE
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AddHosteliteActivity.ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtras(details);
        sendBroadcast(broadcastIntent);
    }

}
