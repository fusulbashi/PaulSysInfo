package paul.com.paulsysinfo.tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by paul on 15.09.16.
 */

public class AndroidToolbox {


    public static String convertIntToString (int value) {
        try{
            return String.valueOf(value);
        }catch (Exception exp) {
            return null;
        }
    }

   /* public static void checkPermission (final Activity activity, String permission){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{permission}, 1);
            }

        }*/
    public static boolean checkPermission (final Activity activity, String permission){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{permission}, 1);
                return false;
            }
      return true;
    }
}
