package com.sourmilq.sourmilq;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.Adapters.*;
import com.sourmilq.sourmilq.Utilities.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-11-20.
 */

public class RecipeActivity extends AppCompatActivity{

    private RecyclerView rArticlesList;
    private RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;
    private ProgressBar progressBar;
    private Model model;

    private Button btnReconnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NetworkUtil.isConnected(getApplicationContext())){
            setUpRecipePage();
            return;
        }
        setContentView(R.layout.offline_recipe);
        btnReconnect = (Button)findViewById(R.id.reconnect);
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtil.isConnected(getApplicationContext())){
                    Toast.makeText(RecipeActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                setUpRecipePage();
            }
        });
    }

    private void setUpRecipePage(){
        setContentView(R.layout.recipe_list_view);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        model = Model.getInstance(getApplicationContext());

        rArticlesList = (RecyclerView) findViewById(R.id.articleList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this, recipes);
        rArticlesList.setAdapter(recipeRecyclerViewAdapter);
        rArticlesList.setLayoutManager(new LinearLayoutManager(this));

        model.getRecipe();

        rArticlesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = recipeRecyclerViewAdapter.getItemCount();
                int firstVisibleItem = recipeRecyclerViewAdapter.getCurrentPostion();

                if(model.getRecipeOffset()-2<firstVisibleItem){
                    model.getRecipe();
                    Log.e("blah","need to load more");
                }
            }
        });
    }
}
