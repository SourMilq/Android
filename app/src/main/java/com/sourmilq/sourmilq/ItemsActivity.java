package com.sourmilq.sourmilq;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sourmilq.sourmilq.Adapters.ItemTabsPagerAdapter;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Fragments.GroceryListItemsFragment;
import com.sourmilq.sourmilq.Fragments.PantryItemsFragment;
import com.sourmilq.sourmilq.Utilities.NetworkUtil;

public class ItemsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroceryListItemsFragment.OnFragmentInteractionListener,
        PantryItemsFragment.OnFragmentInteractionListener {

    public boolean expirationWarned = false;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.items);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ItemTabsPagerAdapter adapter = new ItemTabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        this.model = Model.getInstance(getApplicationContext());
        model.dequeueTasks();
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
            if(NetworkUtil.isConnected(getApplicationContext())) {
                Intent intent = new Intent(ItemsActivity.this, RecipeRecommendationActivity.class);
                startActivity(intent);
                finish();
            }else{
                Snackbar.make(findViewById(android.R.id.content), "No Internet, connect to access recipes recommendations", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else if (id == R.id.recipe) {
            if(NetworkUtil.isConnected(getApplicationContext())) {
                Intent intent = new Intent(ItemsActivity.this, RecipeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Snackbar.make(findViewById(android.R.id.content), "No Internet, connect to access recipes", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else if (id == R.id.logout) {
            model.logout();
            Intent intent = new Intent(ItemsActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.items) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onGroceryListItemsFragmentInteraction(Uri uri) {

    }

    public void onPantryItemsFragmentInteraction(Uri uri) {

    }
}
