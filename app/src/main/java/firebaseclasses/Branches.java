package firebaseclasses;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Poornesh on 03-04-2017.
 */

public  class Branches {
    @NonNull
    public static String getBranch(String USN){
        String branch="BBBBBBBBBB";
        try{
            branch=USN.trim().substring(5,7);
        }catch (StringIndexOutOfBoundsException e){
            Log.d("INCORRECT","USN");
        }

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
