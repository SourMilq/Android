package com.sourmilq.sourmilq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sourmilq.sourmilq.Adapters.RecipeRecyclerViewAdapter;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView rArticlesList;
    private RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;
    private ProgressBar progressBar;
    private Model model;
    private Button btnReconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpRecipePage();
    }

    private void setUpRecipePage(){
//        setContentView(R.layout.recipe_list_view);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        model = Model.getInstance(getApplicationContext());

        rArticlesList = (RecyclerView) findViewById(R.id.articleList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this, false);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.recipeRecommendations) {
            Intent intent = new Intent(RecipeActivity.this, RecipeRecommendationActivity.class);
            startActivity(intent);
        } else if (id == R.id.recipe) {
        } else if (id == R.id.logout) {
            Intent intent = new Intent(RecipeActivity.this, SplashScreenActivity.class);
            startActivity(intent);
        } else if (id == R.id.items) {
            Intent intent = new Intent(RecipeActivity.this, ItemsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
