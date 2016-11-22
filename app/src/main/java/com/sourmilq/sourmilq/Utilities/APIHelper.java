package com.sourmilq.sourmilq.Utilities;

import android.util.Log;

import com.sourmilq.sourmilq.DataModel.HttpObject;
import com.sourmilq.sourmilq.DataModel.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ajanthan on 16-10-15.
 */
public class APIHelper {

    private static String className = "APIHelper";

    private static StringBuilder sb = new StringBuilder();
    private static String domain = "http://ec2-35-163-95-143.us-west-2.compute.amazonaws.com:3000";

    public static String signup(JSONObject jsonObject) throws IOException, JSONException {
        String url = domain + "/v1/user/create";
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url,jsonObject);
            httpObject = HttpRequestHelper.postRequest(httpObject);
            if (200 <= httpObject.getHttpCode() && httpObject.getHttpCode() <= 299) {
                JSONObject jsonObj = new JSONObject(httpObject.getResult());
                if(!(jsonObj.getString("token") !=null) || !jsonObj.getString("token").isEmpty()) {
                    return jsonObj.getString("token");
                }else{
                    Log.e(className, "Token was not retrieved");
                }

            }else{
                Log.e(className, "ErrorCode "+httpObject.getHttpCode()+" was returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String login(JSONObject jsonObject) throws IOException, JSONException {
        String url = domain + "/v1/user";
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url,jsonObject);
            httpObject = HttpRequestHelper.postRequest(httpObject);

            if (200 <= httpObject.getHttpCode() && httpObject.getHttpCode() <= 299) {
                JSONObject jsonObj = new JSONObject(httpObject.getResult());
                if(!(jsonObj.getString("token") !=null) || !jsonObj.getString("token").isEmpty()) {
                    return jsonObj.getString("token");
                }else{
                    Log.e(className, "Token was not retrieved");
                }

            }else{
                Log.e(className, "ErrorCode "+httpObject.getHttpCode()+" was returned");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void addItem(JSONObject jsonObject, long listId, String token) throws IOException, JSONException {
        String url = domain + "/v1/list/" + listId + "/item/add";
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.POST,url,jsonObject);
            httpObject.setToken(token);
            HttpRequestHelper.postRequest(httpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(String token, long id, long listId) throws IOException, JSONException {
        String url = domain + "/v1/list/" + listId + "/item/" + id;
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.DELETE,url);
            httpObject.setToken(token);
            HttpRequestHelper.deleteRequest(httpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkOffItem(String token, long id, long listId) throws IOException, JSONException {
        String url = domain + "/v1/list/" + listId + "/item/" + id + "/done";
        try {
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.POST,url);
            httpObject.setToken(token);
            HttpRequestHelper.postRequest(httpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Long> getLists(String token) {
        String url = domain + "/v1/lists";
        Map<String, Long> listIds = new HashMap<>();
        ArrayList<Long> ret = new ArrayList<>();
        ret.add(0l);
        ret.add(0l);
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url);
            httpObject.setToken(token);
            String result = HttpRequestHelper.getRequest(httpObject);
            JSONObject jsonObj = new JSONObject(result);
            JSONArray lists = (JSONArray) jsonObj.get("lists");
            for (int i = 0; i < lists.length(); i++) {
                JSONObject list = lists.getJSONObject(i);
                if (list.get("name").equals("Grocery List")) {
                    ret.remove(0);
                    ret.add(0, list.getLong("id"));
                } else if (list.get("name").equals("Fridge")) {
                    ret.remove(1);
                    ret.add(1, list.getLong("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<Item> getListItems(String token, long listId) {
        String url = domain + "/v1/list/" + listId;
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url);
            httpObject.setToken(token);
            String result = HttpRequestHelper.getRequest(httpObject);
            return parseItems(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setExpiration(String token, long listId, JSONObject jsonObject, long itemId) {
        String url = domain + "/v1/list/"+ listId+"/item/"+ itemId+"/update";
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.POST,url,jsonObject);
            httpObject.setToken(token);
            httpObject = HttpRequestHelper.postRequest(httpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Item> parseItems(String result) {
        ArrayList<Item> items = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject list = (JSONObject) jsonObj.get("list");
            JSONArray jsonItems = (JSONArray) list.get("items");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                Item newItem = new Item(item.getString("name"),
                        item.getInt("quantity"),
                        item.getDouble("price"),
                        item.getLong("id"));
                if (item.has("expiration") &&
                        !item.getString("expiration").isEmpty() &&
                        !item.getString("expiration").equals("null")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(item.getString("expiration"));
                    Calendar expiration = new GregorianCalendar();
                    expiration.set(date.getYear(), date.getMonth(), date.getDay(), 12, 0, 0);
                    newItem.setExpiration(expiration);
                }
                items.add(newItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

}
