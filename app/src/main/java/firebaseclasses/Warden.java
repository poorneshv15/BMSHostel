package firebaseclasses;

import io.realm.RealmObject;

/**
 * Created by Poornesh on 22-04-2017.
 */

public class Warden extends RealmObject{
    public String name,email,mobile;

    public Warden() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Warden(String name, String email,String mobile) {
        this.name = name;
        this.email = email;
        this.mobile=mobile;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
