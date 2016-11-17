package com.sourmilq.sourmilq.Utilities;

import android.util.Log;

import com.sourmilq.sourmilq.DataModel.HttpObject;
import com.sourmilq.sourmilq.DataModel.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
            if(httpObject.getHttpCode()==200) {
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

            if(httpObject.getHttpCode()==200) {
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
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url,jsonObject);
            httpObject.setToken(token);
            HttpRequestHelper.postRequest(httpObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String deleteItem(String token, long id, long listId) throws IOException, JSONException {
        String url = domain + "/v1/list/" + listId + "/item/" + id;
        try {
            //constants
            HttpObject httpObject = new HttpObject(HttpObject.RequestType.GET,url);
            httpObject.setToken(token);
            HttpRequestHelper.deleteRequest(httpObject);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static long getLists(String token) {
        String url = domain + "/v1/lists";
        Map<String, Long> listIds = new HashMap<>();
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
                    return list.getLong("id");
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
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

    private static ArrayList<Item> parseItems(String result) {
        ArrayList<Item> items = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject list = (JSONObject) jsonObj.get("list");
            JSONArray jsonItems = (JSONArray) list.get("items");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                items.add(new Item(item.getString("name"),
                        item.getInt("quantity"),
                        item.getDouble("price"),
                        item.getLong("id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

}
