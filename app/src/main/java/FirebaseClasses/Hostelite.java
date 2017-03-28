package FirebaseClasses;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poornesh on 27-03-2017.
 */

public class Hostelite {
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
    String hostel;
    boolean hostelite;
    int roomNo;


    public Hostelite(String name, String email, String hostel,int roomNo, String usn, String mobile, String fatherName, String fatherAddress, String fatherMobile, String guardianName, String guardianAddress, String guardianMobile) {
        this.name = name;
        this.email = email;
        this.usn = usn;
        this.mobile = mobile;
        this.fatherName = fatherName;
        this.fatherAddress = fatherAddress;
        this.fatherMobile = fatherMobile;
        this.guardianName = guardianName;
        this.guardianAddress = guardianAddress;
        this.guardianMobile = guardianMobile;
        this.hostel = hostel;
        this.hostelite = true;
        this.roomNo=roomNo;
    }
    public Hostelite(String name,String email, String hostel, int roomNo) {
        this.name = name;
        this.email = email;
        this.usn = null;
        this.mobile = null;
        this.fatherName = null;
        this.fatherAddress = null;
        this.fatherMobile = null;
        this.guardianName = null;
        this.guardianAddress = null;
        this.guardianMobile = null;
        this.hostel = hostel;
        this.hostelite = true;
        this.roomNo=roomNo;
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
        result.put("hostel/"+hostel+"/"+roomNo, true);

        return result;
    }
}
