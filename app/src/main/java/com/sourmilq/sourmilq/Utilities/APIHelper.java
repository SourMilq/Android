package com.sourmilq.sourmilq.Utilities;

import android.util.Log;

import com.sourmilq.sourmilq.DataModel.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ajanthan on 16-10-15.
 */
public class APIHelper {

    private static StringBuilder sb = new StringBuilder();
    private static String domain = "http://4dd95a2a.ngrok.io";

    public static String signup(JSONObject jsonObject) throws IOException, JSONException {
        String endpointDomain = domain + "/v1/user/create";
        try {
            //constants
            URL url = new URL(endpointDomain);
            String result = HttpRequestHelper.postRequest("", jsonObject, url);
            JSONObject jsonObj = new JSONObject(result);
            return jsonObj.getString("token");

        } catch (Exception e) {
            return null;
        }
    }

    public static String login(JSONObject jsonObject) throws IOException, JSONException {
        String endpointDomain = domain + "/v1/user";
        try {
            //constants
            URL url = new URL(endpointDomain);
            Log.e("TOKEN: ", endpointDomain);
            String result = HttpRequestHelper.postRequest("", jsonObject, url);
            JSONObject jsonObj = new JSONObject(result);
            return jsonObj.getString("token");

        } catch (Exception e) {
            return null;
        }
    }

    public static void addItem(JSONObject jsonObject, long listId, String token) throws IOException, JSONException {
        String endpointDomain = domain + "/v1/list/" + listId + "/item/add";
        try {
            //constants
            URL url = new URL(endpointDomain);
            Log.e("ADD1: ", endpointDomain);
            Log.e("ADD1: ", jsonObject.toString());
            HttpRequestHelper.postRequest(token, jsonObject, url);
        } catch (Exception e) {
        }
    }

    public static String deleteItem(String token, long id, long listId) throws IOException, JSONException {
        String endpointDomain = domain + "/v1/list/"+listId+"/item/"+id;
        try {
            //constants
            URL url = new URL(endpointDomain);
            HttpRequestHelper.deleteRequest(token, url);
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    public static long getLists(String token) {
        String endpointDomain = domain + "/v1/lists";
        Map<String, Long> listIds = new HashMap<>();
        try {
            //constants
            URL url = new URL(endpointDomain);
            Log.e("IDS", "test");
            String result = HttpRequestHelper.getRequest(token, url);
            Log.e("IDS", result);
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
        String endpointDomain = domain + "/v1/list/" + listId;

        Log.e("TESTING: ", listId + "");
        try {
            //constants
            URL url = new URL(endpointDomain);
            String result = HttpRequestHelper.getRequest(token, url);
            Log.e("TESTING: ", result);
            return parseItems(result);
        } catch (Exception e) {
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
            return null;
        }
        return items;
    }

}
