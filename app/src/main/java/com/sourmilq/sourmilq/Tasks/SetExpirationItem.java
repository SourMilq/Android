package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class SetExpirationItem extends AsyncTask<JSONObject, Void, Void> {

    private Long listId;
    private Item item;
    private String token;
    private Model model;

    public SetExpirationItem(Long listId, Item item, String token, Model model) {
        this.listId = listId;
        this.item = item;
        this.token = token;
        this.model = model;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {

        //Todo: add parameter checking
        JSONObject jsonObject = new JSONObject();
        try {
            //TODO: make json object
            String expString = "";
            if (item.getExpiration() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                expString = sdf.format(item.getExpiration().getTime());
            }
            jsonObject.put("expiration", expString);
            APIHelper.setExpiration(token,listId,jsonObject,item.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        model.finishedTasks();
        model.dequeueTasks();
    }
}

