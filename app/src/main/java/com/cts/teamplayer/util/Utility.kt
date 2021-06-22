package com.cts.teamplayer.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import java.util.regex.Pattern


class Utility {

    var locationManager: LocationManager? = null

    /* LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
  */
    var context: Context? = null

    companion object {
        const val MY_PERMISSIONS_GPS = 222
        var UID = ""

        //PendingResult<LocationSettingsResult> pendingResult;
        fun isValidEmail(email: String?): Boolean {
            var isValid = false
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (matcher.matches()) {
                isValid = true
            }
            return isValid
        }

        /* public static boolean isLocationEnabled(Activity mActivity) {
        LocationManager lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            enableLocationSettingDialog(mActivity, "Please turn on Your GPS");
        } else {
            return true;
        }
        return false;
    }
*/
        fun enableLocationSettingDialog(mActivity: Activity, message: String?) {
            /*  AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(mActivity.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mActivity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), MY_PERMISSIONS_GPS);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();*/
            val builder = AlertDialog.Builder(mActivity)
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { dialog, id -> mActivity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton(
                    "No"
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }
}
