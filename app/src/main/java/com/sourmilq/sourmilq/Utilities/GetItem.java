package com.sourmilq.sourmilq.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class GetItem extends AsyncTask<JSONObject, Void, ArrayList<Item>> {
    private Model model;

    public GetItem(){
        model = Model.getInstance();
    }

    @Override
    protected ArrayList<Item> doInBackground(JSONObject... params) {
        ArrayList<Item> items;
        Log.e("TESTING: ","test");
        items = APIHelper.getListItems(model.getToken(),model.getGroceryListId());

        Log.e("TESTING: ",items.size()+"'");
        return items;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> a) {
        model.setGroceryItems(a);
        super.onPostExecute(a);
    }
}

