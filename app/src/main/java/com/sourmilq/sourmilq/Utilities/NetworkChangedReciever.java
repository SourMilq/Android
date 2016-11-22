package com.sourmilq.sourmilq.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Model;

/**
 * Created by ajanthan on 16-10-15.
 */
public class NetworkChangedReciever extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, status,
                Toast.LENGTH_LONG).show();
        if(NetworkUtil.getConnectivityStatus(context)!= NetworkUtil.NetworkStatus.TYPE_NOT_CONECTED){
            Model.getInstance(context).updateList();
        }
    }
}
