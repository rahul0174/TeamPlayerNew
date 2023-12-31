package com.cts.teamplayer.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by RAm on 7/12/16.
 */

public class MarshmallowPermission {

    Activity mActivity;

    public MarshmallowPermission(Activity mActivity) {
        this.mActivity = mActivity;
    }
    /**
     * To call this method.
     * @param permissionName name of permission.
     * @param requestCode    code for callback
     */
    public boolean isPermissionGranted(String permissionName, int requestCode) {
        if (ContextCompat.checkSelfPermission(mActivity, permissionName) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{permissionName}, requestCode);
        } else {
            return true;
        }
        return false;
    }

    /**
     * To call this method.
     * @param requestCode code for callback.
     * @param permissions array of permissioms.
     */
    public boolean isPermissionGrantedAll(int requestCode, String... permissions) {
        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
        } else {
            return true;
        }
        return false;
    }

    /**
     * check already have permission with the current application.
     */
    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mActivity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
