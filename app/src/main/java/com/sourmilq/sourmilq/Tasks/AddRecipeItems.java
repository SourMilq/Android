package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class AddRecipeItems extends AsyncTask<JSONObject, Void, Void> {
    private Model model;
    private long itemId;

    public AddRecipeItems(Model model, long itemId) {
        this.model = model;
        this.itemId = itemId;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {
        APIHelper.addRecipeItems(model.getToken(), itemId);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        model.finishedTasks();
        model.dequeueTasks();
    }
}

