package com.sourmilq.sourmilq.Utilities;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class AddDeleteItem extends AsyncTask<JSONObject, Void, Void> {
    public enum ActionType {ADD, DELETE}

    private ActionType actionType;
    private Model model;
    private Long listId;
    private Item item;
    private onCallCompleted listener;

    public AddDeleteItem(ActionType actionType, Long listId, Item item, onCallCompleted listener) {
        this.listener = listener;
        this.actionType = actionType;
        this.listId = listId;
        this.item = item;
        model = Model.getInstance();
    }

    @Override
    protected Void doInBackground(JSONObject... params) {

//        if (params.length > 0) {
            if (actionType == ActionType.ADD) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("item",item.getJson());
                    APIHelper.addItem(jsonObject,listId, model.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (actionType == ActionType.DELETE) {
                try {
                    APIHelper.deleteItem(model.getToken(), item.getId(), model.getGroceryListId());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        model.updateGroceryList();
        super.onPostExecute(aVoid);
    }
}

