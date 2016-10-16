package com.sourmilq.sourmilq.Utilities;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ajanthan on 16-10-15.
 */
public class APIHelper {
    public static String postRequest(JSONObject jsonObject, URL url) throws IOException {
        String message = jsonObject.toString();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout( 10000 /*milliseconds*/ );
        conn.setConnectTimeout( 15000 /* milliseconds */ );
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);

        //make some HTTP header nicety
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        //open
        conn.connect();

        //setup send
        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
        os.write(message.getBytes());
        //clean up
        os.flush();

        //do somehting with response
        InputStream is = conn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        return out.toString();
    }

}

