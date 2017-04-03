package com.psps.projects.bmshostel.realmpackage;

import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by Poornesh on 03-04-2017.
 */

public class RealmHelper {
    public static void addHostelite(Realm realm, final Bundle hostelite){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Hostelite user = bgRealm.createObject(Hostelite.class);
                user.setName(hostelite.getString("name"));
                user.setEmail(hostelite.getString("email"));
                user.setUsn(hostelite.getString("usn"));
                user.setMobile(hostelite.getString("mobile"));
                user.setFatherName(hostelite.getString("fName"));
                user.setFatherMobile(hostelite.getString("fMobile"));
                user.setFatherAddress(hostelite.getString("fAddress"));
                user.setGuardianName(hostelite.getString("gName"));
                user.setGuardianMobile(hostelite.getString("gMobile"));
                user.setGuardianAddress(hostelite.getString("gAddress"));
                user.setHostel(hostelite.getString("hostel"));
                user.setRoomNo(hostelite.getInt("roomNo"));
                user.setSem(6);
                user.setBranch("CSE");
                user.setHostelite(true);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.d("REALM ADD HOSTELITE ","Success");
                //Toast.makeText(AddHosteliteActivity.this, "Student Added", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("REALM ADD HOSTELITE ",error.getMessage());
            }
        });
    }
}
