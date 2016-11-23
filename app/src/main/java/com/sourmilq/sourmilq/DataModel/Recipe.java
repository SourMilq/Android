package com.sourmilq.sourmilq.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ajanthan on 2016-11-20.
 */

public class Recipe implements Serializable{

    private long id;
    private String title;
    private String text;
    private String preparationTime;
    private String imageUrl;
    boolean isVegetarian;
    boolean isVegan;
    ArrayList<RecipeIngredients> ingredients;

    public Recipe(long id, String title, String text, String preparationTime, String imageUrl) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.preparationTime = preparationTime;
        this.imageUrl = imageUrl;
        ingredients = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public ArrayList<RecipeIngredients> getIngredients(){
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredients> ingredients){
        this.ingredients = ingredients;
    }
}
