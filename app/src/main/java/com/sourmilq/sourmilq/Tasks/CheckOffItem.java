package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class CheckOffItem extends AsyncTask<JSONObject, Void, Void> {
    private Long listId;
    private Item item;
    private String token;

    public CheckOffItem(Long listId, Item item, String token) {
        this.listId = listId;
        this.item = item;
        this.token = token;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {

        //Todo: add parameter checking
        try {
            APIHelper.checkOffItem(token, item.getId(), listId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

