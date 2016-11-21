package com.sourmilq.sourmilq.DataModel;

import java.io.Serializable;

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

    public Recipe(long id, String title, String text, String preparationTime, String imageUrl) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.preparationTime = preparationTime;
        this.imageUrl = imageUrl;
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
}
