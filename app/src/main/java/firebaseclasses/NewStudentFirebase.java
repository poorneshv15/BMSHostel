package firebaseclasses;

import io.realm.RealmObject;

/**
 * Created by patil on 03-03-2017.
 */

public class NewStudentFirebase {
    public String usn;
    public boolean hostelite;
    public String email;
    public String name;
    public String uriPhoto;

    public NewStudentFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public NewStudentFirebase(String name, String email, String usn) {
        this.usn = usn;
        this.hostelite=false;
        this.email=email;
        this.name=name;
        this.uriPhoto=null;
    }
    public NewStudentFirebase(String name, String email, String usn, String uriPhoto) {
        this.usn = usn;
        this.hostelite=false;
        this.email=email;
        this.name=name;
        this.uriPhoto=uriPhoto;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
