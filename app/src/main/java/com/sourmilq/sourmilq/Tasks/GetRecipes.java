package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class GetRecipes extends AsyncTask<JSONObject, Void, ArrayList<Recipe>> {
    private Model model;
    private int offset;

    public GetRecipes(Model model, int offset) {
        this.model = model;
        this.offset = offset;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(JSONObject... params) {
        ArrayList<Recipe> recipes;
        recipes = APIHelper.getRecipe(model.getToken(), offset);
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> a) {
        model.addRecipes(a);
        model.recipeRecieved();
        super.onPostExecute(a);
    }
}

