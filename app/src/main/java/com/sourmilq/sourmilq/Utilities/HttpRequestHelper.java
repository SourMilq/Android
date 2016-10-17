package com.sourmilq.sourmilq.Utilities;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ajanthan on 16-10-15.
 */
public class HttpRequestHelper {
    public static String postRequest(String token, JSONObject jsonObject, URL url) {

        String message = jsonObject.toString();
        Log.e("ADD: ","test10");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout( 10000 /*milliseconds*/ );
        conn.setConnectTimeout( 15000 /* milliseconds */ );
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);

        Log.e("ADD: ","test11");
        //make some HTTP header nicety
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        if(!token.isEmpty()) {
            Log.e("ADD: ","token");
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        //open
        try {
            conn.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.e("ADD: ","test12");
        //setup send
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(conn.getOutputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            os.write(message.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //clean up
        try {
            os.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Log.e("ADD: ","test13");
        //do somehting with response
        InputStream is = null;

        try {
            is = conn.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Log.e("ADD: ","test14");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        Log.e("ADD: ","test15");

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("ADD: ",out.toString());

        return out.toString();

    }

    public static String getRequest(String token, URL url) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer "+token);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        Log.e("ADD: ",result.toString());
        return result.toString();
    }

    public static void deleteRequest(String token, URL url) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization","Bearer "+token);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        Log.e("ADD: ",result.toString());
    }
}

