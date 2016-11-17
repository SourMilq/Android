package com.sourmilq.sourmilq.DataModel;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ajanthan on 2016-11-17.
 */

public class HttpObject  {
    public enum Status {CREATED, SENT, BLOCKED, RECIEVED}
    public enum RequestType {GET, POST, DELETE}

    private long httpCode;
    URL url;
    JSONObject body;
    String result;
    String token ="";
    Status status;
    RequestType requestType;

    public HttpObject(RequestType requestType, String url) throws MalformedURLException {
        this.url = new URL(url);
        this.requestType = requestType;
    }

    public HttpObject(RequestType requestType, String url, JSONObject body) throws MalformedURLException {
        this(requestType, url);
        this.body = body;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(long httpCode) {
        this.httpCode = httpCode;
    }

    public URL getUrl() {
        return url;
    }

    public JSONObject getBody() {
        return body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
