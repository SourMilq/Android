package com.sourmilq.sourmilq;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.DataModel.RecipeIngredients;
import com.squareup.picasso.Picasso;

/**
 * Created by ajanthan on 2016-11-20.
 */


public class RecipeDetailActivity extends AppCompatActivity {

    private final String recipeKey = "recipe";

    private TextView tvTitle;
    private TextView tvText;
    private TextView tvPreparationTime;
    private TextView tvIngredients;
    private ImageView ivImage;
    private FloatingActionButton fabFavourite;

    private Recipe recipe;
    private Model model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_view);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = Model.getInstance(getApplicationContext());
        tvTitle = (TextView) findViewById(R.id.title);
        tvText = (TextView) findViewById(R.id.body);
        tvPreparationTime = (TextView) findViewById(R.id.preparationTime);
        tvIngredients = (TextView) findViewById(R.id.ingredients);
        ivImage = (ImageView) findViewById(R.id.img);
        fabFavourite = (FloatingActionButton) findViewById(R.id.favouriteButton);

        recipe = (Recipe) getIntent().getSerializableExtra(recipeKey);

        tvTitle.setText(recipe.getTitle());
        tvText.setText(recipe.getText());
        tvPreparationTime.setText("Preparation Time: " + recipe.getPreparationTime() + " minutes");
        Picasso.with(this)
                .load(recipe.getImageUrl())
                .into(ivImage);
        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Adding ingredients to grocery list", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                model.addRecipeItem(recipe);
            }
        });
        String ingredients = "";
        for(RecipeIngredients r:recipe.getIngredients()){
            ingredients+="- "+r.getName()+"\n";
        }
        tvIngredients.setText(ingredients);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
