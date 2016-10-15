package com.sourmilq.sourmilq.DataModel;

import java.util.ArrayList;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Model {
    private static Model instance = null;

    private ArrayList<Item> groceryItems;
    private ArrayList<Item> pantryItems;


    private Model() {
        groceryItems =  new ArrayList<>();
        pantryItems =  new ArrayList<>();
    }

    public static Model getInstance() {
        if(instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public ArrayList<Item> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(ArrayList<Item> groceryItems) {
        this.groceryItems = groceryItems;
    }

    public ArrayList<Item> getPantryItems() {
        return pantryItems;
    }

    public void setPantryItems(ArrayList<Item> pantryItems) {
        this.pantryItems = pantryItems;
    }

}
