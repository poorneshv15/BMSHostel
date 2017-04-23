package firebaseclasses;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.psps.projects.bmshostel.Hostel;
import com.psps.projects.bmshostel.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

import static android.content.ContentValues.TAG;


public class DeleteHosteliteService extends IntentService  implements Serializable{
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * m name Used to name the worker thread, important only for debugging.
     */
    public DeleteHosteliteService() {
        super("Delete Students");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    int[] currentCapacity;
    int[] roomsUnderControl;
    int successful=1;
    List<Integer> rooms;
    NotificationManager notificationManager;
    Notification myNotification;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ArrayList<String> emails;
        if (intent != null) {
            emails = intent.getStringArrayListExtra("emails");
        }
        else
        {
            Log.d(TAG," Couldnt Delete Hostelites");
            return;
        }
        Log.d("DELETE SERVICE", "To Be Deleted" + Arrays.toString(emails.toArray()));
        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Deleting")
                .setContentText(emails.size()+" hostelites")
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        notificationManager.notify(7, myNotification);
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //
                Hostel hostel=realm.where(Hostel.class).findFirst();
                currentCapacity=hostel.getCurrentCapacity();
                roomsUnderControl=hostel.getRoomsUnderCotrol();
                rooms=new ArrayList<Integer>() {{ for (int i : roomsUnderControl) add(i); }};



            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference userRef = userSnapshot.getRef();
                    if(userSnapshot.child("hostelite").getValue().equals(false)){
                        return;
                    }
                    userRef.child("hostelite").setValue(false);
                    userRef.child("hostelName").setValue(null);
                    userRef.child("roomNo").setValue(null);
                    Log.d("User Snapshot", userSnapshot.toString());
                    ref.child("hostel").child(String.valueOf(userSnapshot.child("hostelName").getValue()))
                            .child(String.valueOf(userSnapshot.child("roomNo").getValue()))
                            .child("hostelites")
                            .child(userSnapshot.getKey())
                            .child(userSnapshot.getKey())
                            .setValue(null);
                    Log.d("DELETE SERVICE",String.valueOf(userSnapshot.child("hostelName").getValue()));

                    try{
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                currentCapacity[rooms.indexOf(Integer.parseInt(String.valueOf(userSnapshot.child("roomNo").getValue())))]--;
                                realm.where(Hostel.class).findFirst().setCurrentCapacity(currentCapacity);
                                realm.where(Hostelite.class).equalTo("email", String.valueOf(userSnapshot.child("email").getValue())).findFirst().deleteFromRealm();
                            }
                        });
                    }
                    catch (Exception e){
                        Log.d(TAG,"Null pointer Exception Or "+e.getMessage());
                    }

                    Log.d(TAG,"Deleted..."+userSnapshot.child("email"));
                    myNotification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle("Deleting")
                            .setContentText((emails.size()-successful)+" hostelites")
                            .setTicker("Notification!")
                            .setWhen(System.currentTimeMillis())
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                            .build();

                    notificationManager.notify(7, myNotification);
                    myNotification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle("Deleted")
                            .setContentText((successful++)+" hostelites")
                            .setTicker("Notification!")
                            .setWhen(System.currentTimeMillis())
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                            .build();

                    notificationManager.notify(8, myNotification);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        };


        for (String email : emails) {
            Query applesQuery = ref.child("users").orderByChild("email").equalTo(email);
            applesQuery.addValueEventListener(valueEventListener);
            Log.d(TAG,"Deleting..."+email);
        }
    }
}
