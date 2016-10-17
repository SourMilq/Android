package com.sourmilq.sourmilq.DataModel;

import android.util.Log;

import com.sourmilq.sourmilq.Utilities.AddDeleteItem;
import com.sourmilq.sourmilq.Utilities.GetItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Model extends Observable {
    private static Model instance = null;

    private ArrayList<Item> groceryItems;
    private ArrayList<Item> pantryItems;
    private long groceryListId;
    private long pantryListId;
    private String token;

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

    public boolean hasValidToken(){
        return token != null;
    }

    public ArrayList<Item> getGroceryItems() {
        return groceryItems;
    }

    public void updateGroceryList(){
        GetItem getItem = new GetItem();
        getItem.execute();
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

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Long getGroceryListId(){
        return groceryListId;
    }

    public void setListIds(long id){
        groceryListId  =id;
        Log.e("IDS", groceryListId +"");
    }

    public void addItem(Item item){
        AddDeleteItem addDeleteItem = new AddDeleteItem(AddDeleteItem.ActionType.ADD,groceryListId, item);
        addDeleteItem.execute();
    }

    public void deleteItem(Item item){
        AddDeleteItem addDeleteItem = new AddDeleteItem(AddDeleteItem.ActionType.DELETE,groceryListId, item);
        addDeleteItem.execute();
    }
}
