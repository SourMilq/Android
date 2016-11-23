package com.sourmilq.sourmilq.DataModel;

import java.io.Serializable;

/**
 * Created by ajanthan on 2016-11-22.
 */

public class RecipeIngredients implements Serializable{

    private String name;
    private String quantity;

    public RecipeIngredients(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

}
