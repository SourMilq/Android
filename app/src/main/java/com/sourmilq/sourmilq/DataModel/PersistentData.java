package com.sourmilq.sourmilq.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Philip on 2016-11-16.
 */

public class PersistentData implements Serializable {
    public static String PERTISTENT_DATA_FILENAME = "localdata.sourmilq";

    public ArrayList<Item> groceryItems;
    public ArrayList<Item> pantryItems;
    public long groceryListId;
    public long pantryListId;
    public String token;

    public PersistentData(Model m) {
        groceryItems = m.getGroceryItems();
        pantryItems = m.getPantryItems();
        groceryListId = m.getGroceryListId();
        pantryListId = m.getPantryListId();
        token = m.getToken();
    }
}
