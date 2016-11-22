package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class GetRecipeRecommendation extends AsyncTask<JSONObject, Void, ArrayList<Recipe>> {
    private Model model;

    public GetRecipeRecommendation(Model model) {
        this.model = model;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(JSONObject... params) {
        ArrayList<Recipe> recipes;
        recipes = APIHelper.getRecipeRecommentations(model.getToken());
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> a) {
        model.setRecipesRecommendations(a);
        model.applyChanges();
        super.onPostExecute(a);
    }
}

