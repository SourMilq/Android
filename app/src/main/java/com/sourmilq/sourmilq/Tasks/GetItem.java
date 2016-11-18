package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class GetItem extends AsyncTask<JSONObject, Void, ArrayList<Item>> {
    private Model model;
    private long listId;

    public GetItem(Model model, long listId) {
        this.model = model;
        this.listId = listId;
    }

    @Override
    protected ArrayList<Item> doInBackground(JSONObject... params) {
        ArrayList<Item> items;
        items = APIHelper.getListItems(model.getToken(), listId);
        return items;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> a) {
        if (listId == model.getGroceryListId()) {
            model.setGroceryItems(a);
        } else if (listId == model.getPantryListId()) {
            model.setPantryItems(a);
        }
        super.onPostExecute(a);
    }
}

