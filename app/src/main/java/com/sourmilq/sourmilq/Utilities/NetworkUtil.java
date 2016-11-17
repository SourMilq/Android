package com.sourmilq.sourmilq.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ajanthan on 16-10-15.
 */
public class NetworkUtil {

    public enum NetworkStatus {
        TYPE_WIFI, TYPE_MOBILE, TYPE_NOT_CONECTED
    }

    ;

    public static NetworkStatus getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return NetworkStatus.TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return NetworkStatus.TYPE_MOBILE;
        }
        return NetworkStatus.TYPE_NOT_CONECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        NetworkStatus conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkStatus.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkStatus.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkStatus.TYPE_NOT_CONECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static boolean isConnected(Context context) {
        NetworkStatus conn = NetworkUtil.getConnectivityStatus(context);
        return conn != NetworkStatus.TYPE_NOT_CONECTED;
    }
}
