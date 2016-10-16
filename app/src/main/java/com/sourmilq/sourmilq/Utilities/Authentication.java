package com.sourmilq.sourmilq.Utilities;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Authentication {

    private static StringBuilder sb = new StringBuilder();
    private static String domain = "http://4dd95a2a.ngrok.io";

    public static String signup(JSONObject jsonObject) throws IOException, JSONException {
        String endpointDomain= domain+ "/v1/user/create";
        try {
            //constants
            URL url = new URL(endpointDomain);
            String result = APIHelper.postRequest(jsonObject,url);
            JSONObject jsonObj = new JSONObject(result);
            return jsonObj.getString("token");

        } catch (Exception e){
            return null;
        }
    }

    public static String login(JSONObject jsonObject) throws IOException, JSONException {
            String endpointDomain= domain+ "/v1/user";
        try {
            //constants
            URL url = new URL(endpointDomain);
            String result = APIHelper.postRequest(jsonObject,url);
            JSONObject jsonObj = new JSONObject(result);
            return jsonObj.getString("token");

        } catch (Exception e){
            return null;
        }
    }
}
