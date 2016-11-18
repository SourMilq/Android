package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class AddDeleteItem extends AsyncTask<JSONObject, Void, Void> {


    private Model.ActionType actionType;
    private Long listId;
    private Item item;
    private String token;
    private Model model;

    public AddDeleteItem(Model.ActionType actionType, Long listId, Item item, String token, Model model) {
        this.actionType = actionType;
        this.listId = listId;
        this.item = item;
        this.token = token;
        this.model = model;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {

        //Todo: add parameter checking
        if (actionType == Model.ActionType.ADD) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("item", item.getJson());
                APIHelper.addItem(jsonObject, listId, token);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (actionType == Model.ActionType.DELETE) {
            try {
                APIHelper.deleteItem(token, item.getId(), listId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

