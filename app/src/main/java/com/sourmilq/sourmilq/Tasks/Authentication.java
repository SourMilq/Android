package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class Authentication extends AsyncTask<JSONObject, Void, String> {
    private onCallCompleted listener;

    public enum AuthType {LOGIN, SIGNUP}

    private AuthType authType;
    private Model model;

    public Authentication(onCallCompleted listener, AuthType authType, Model model) {
        this.listener = listener;
        this.authType = authType;
        this.model = model;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        String result = "";
        try {
            if (params.length > 0) {
                if (authType == AuthType.LOGIN) {
                    result = APIHelper.login(params[0]);
                } else if (authType == AuthType.SIGNUP) {
                    result = APIHelper.signup(params[0]);
                }
                if(result!=null || !result.isEmpty()) {
                    model.setToken(result);
                    model.setListIds(APIHelper.getLists(result));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String token) {
        if(token!=null && !token.isEmpty()) {
            listener.onTaskCompleted(true);
        }else{
            listener.onTaskCompleted(false);
        }
        super.onPostExecute(token);
    }
}

