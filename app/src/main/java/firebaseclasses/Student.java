package firebaseclasses;

import io.realm.RealmObject;

/**
 * Created by Poornesh on 22-04-2017.
 */

public class Student extends RealmObject {
    public String usn;
    public boolean hostelite;
    public String email;
    public String name;
    public String uriPhoto;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Student(String name, String email, String usn) {
        this.usn = usn;
        this.hostelite=false;
        this.email=email;
        this.name=name;
        this.uriPhoto=null;
    }
    public Student(String name, String email, String usn, String uriPhoto) {
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
