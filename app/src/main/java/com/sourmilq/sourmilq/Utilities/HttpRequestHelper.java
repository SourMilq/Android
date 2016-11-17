package com.sourmilq.sourmilq.Utilities;

import android.util.Log;

import com.sourmilq.sourmilq.DataModel.HttpObject;

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

    public static HttpObject postRequest(HttpObject httpObject) {
        String message = httpObject.getBody().toString();
        HttpURLConnection conn = null;
        StringBuilder out = new StringBuilder();
        try {
            conn = (HttpURLConnection) httpObject.getUrl().openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            if (!httpObject.getToken().isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + httpObject.getToken());
            }
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            os.flush();
            BufferedReader reader;
            httpObject.setHttpCode(conn.getResponseCode());
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            } else {
                reader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            httpObject.setResult(out.toString());
            Log.e("sadme",out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpObject;

    }

    public static String getRequest(HttpObject httpObject) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) httpObject.getUrl().openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + httpObject.getToken());
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static void deleteRequest(HttpObject httpObject) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) httpObject.getUrl().openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + httpObject.getToken());
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
    }
}

