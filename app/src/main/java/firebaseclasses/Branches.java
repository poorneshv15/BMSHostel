package firebaseclasses;

import android.support.annotation.NonNull;

/**
 * Created by Poornesh on 03-04-2017.
 */

public  class Branches {
    @NonNull
    public static String getBranch(String USN){
        String branch=USN.trim().substring(5,7);
        switch (branch){
            case "CS":
                return "CSE";
            case "IS":
                return "ISE";
            case "ME":
                return "ME";
            default:
                return "N.A.";
        }
    }

    public static int getSem(String USN){

        return 0;
    }
}
