package firebaseclasses;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by Poornesh on 03-04-2017.
 */

public class Hostelite extends RealmObject {
    String name;
    String email;
    String usn;
    String mobile;
    String fatherName;
    String fatherAddress;
    String fatherMobile;
    String guardianName;
    String guardianAddress;
    String guardianMobile;
    String hostelName;
    boolean hostelite;
    int roomNo;
    String branch;
    int sem;

    public Hostelite(){
        //Empty Constructor
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public static Hostelite create(String name, String email, String hostel, int roomNo, String usn, String mobile, String fatherName, String fatherAddress, String fatherMobile, String guardianName, String guardianAddress, String guardianMobile) {
        Hostelite hostelite=new Hostelite();

        hostelite.name = name;
        hostelite.email = email;
        hostelite.usn = usn;
        hostelite.mobile = mobile;
        hostelite.fatherName = fatherName;
        hostelite.fatherAddress = fatherAddress;
        hostelite.fatherMobile = fatherMobile;
        hostelite.guardianName = guardianName;
        hostelite.guardianAddress = guardianAddress;
        hostelite.guardianMobile = guardianMobile;
        hostelite.hostelName = hostel;
        hostelite.hostelite = true;
        hostelite.roomNo=roomNo;
        hostelite.branch= Branches.getBranch(usn);
        hostelite.sem=6;

        return hostelite;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("usn", usn);
        result.put("mobile", mobile);
        result.put("fatherName", fatherName);
        result.put("fatherAddress", fatherAddress);
        result.put("fatherMobile", fatherMobile);
        result.put("guardianName", guardianName);
        result.put("guardianAddress", guardianAddress);
        result.put("guardianMobile", guardianMobile);
        result.put("hostelite", hostelite);
        result.put("roomNo",roomNo);
        result.put("hostelName",hostelName);
        result.put("sem",sem);
        result.put("branch",branch);
        //result.put("hostelName/"+hostelName+"/"+roomNo, true);

        return result;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherAddress() {
        return fatherAddress;
    }

    public void setFatherAddress(String fatherAddress) {
        this.fatherAddress = fatherAddress;
    }

    public String getFatherMobile() {
        return fatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile = fatherMobile;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianAddress() {
        return guardianAddress;
    }

    public void setGuardianAddress(String guardianAddress) {
        this.guardianAddress = guardianAddress;
    }

    public String getGuardianMobile() {
        return guardianMobile;
    }

    public void setGuardianMobile(String guardianMobile) {
        this.guardianMobile = guardianMobile;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public boolean isHostelite() {
        return hostelite;
    }

    public void setHostelite(boolean hostelite) {
        this.hostelite = hostelite;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public String toString() {
        return "Hostelite{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", usn='" + usn + '\'' +
                ", mobile='" + mobile + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", fatherAddress='" + fatherAddress + '\'' +
                ", fatherMobile='" + fatherMobile + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", guardianAddress='" + guardianAddress + '\'' +
                ", guardianMobile='" + guardianMobile + '\'' +
                ", hostelName='" + hostelName + '\'' +
                ", hostelite=" + hostelite +
                ", roomNo=" + roomNo +
                '}';
    }


}
