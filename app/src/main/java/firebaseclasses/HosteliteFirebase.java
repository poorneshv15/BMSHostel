package firebaseclasses;

import io.realm.RealmObject;

/**
 * Created by Poornesh on 04-04-2017.
 */

public class HosteliteFirebase {
    public String name;
    public String email;
    public String usn;
    public String mobile;
    public String fatherName;
    public String fatherAddress;
    public String fatherMobile;
    public String guardianName;
    public String guardianAddress;
    public String guardianMobile;
    public String hostelName;
    public String uriPhoto;
    public boolean hostelite;
    public int roomNo;
    public String branch;
    public int sem;

    public HosteliteFirebase(){
        //Empty Constructor
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
