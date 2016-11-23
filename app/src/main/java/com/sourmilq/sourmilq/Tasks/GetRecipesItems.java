package com.sourmilq.sourmilq.Tasks;

import android.os.AsyncTask;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.DataModel.RecipeIngredients;
import com.sourmilq.sourmilq.Utilities.APIHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-10-16.
 */

public class GetRecipesItems extends AsyncTask<JSONObject, Void, ArrayList<RecipeIngredients>> {
    private Model model;
    private Recipe recipe;

    public GetRecipesItems(Model model, Recipe recipe) {
        this.model = model;
        this.recipe = recipe;
    }

    @Override
    protected ArrayList<RecipeIngredients> doInBackground(JSONObject... params) {
        ArrayList<RecipeIngredients> recipes;
        recipes = APIHelper.getRecipeIngredients(model.getToken(), recipe.getId());
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<RecipeIngredients> a) {
        recipe.setIngredients(a);
        super.onPostExecute(a);
    }
}

