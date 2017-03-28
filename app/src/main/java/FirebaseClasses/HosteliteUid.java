package FirebaseClasses;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poornesh on 26-03-2017.
 */

public class HosteliteUid {
    String uid;

    public HosteliteUid(String uid) {
        this.uid = uid;
    }


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(uid, true);
        return result;
    }
    // [END post_to_map]
}
