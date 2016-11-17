package com.sourmilq.sourmilq.DataModel;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Model {
    private static Model instance = null;

    private ArrayList<Item> groceryItems;
    private ArrayList<Item> pantryItems;
    private String token;

    private Model() {
        groceryItems =  new ArrayList<>();
        pantryItems =  new ArrayList<>();
    }

    public static Model getInstance(Context context) {
        if(instance == null) {
            instance = new Model();
            try {
                FileInputStream fis = context.openFileInput(PersistentData.PERTISTENT_DATA_FILENAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                PersistentData pData = (PersistentData) is.readObject();
                is.close();
                fis.close();
                instance.setGroceryItems(pData.groceryItems);
                instance.setPantryItems(pData.pantryItems);
                // instance.setGroceryListId(pData.groceryListId);
                // instance.setPantryListId(pData.pantryListId);
                instance.setToken(pData.token);
            } catch (IOException | ClassNotFoundException e) {
                Toast.makeText(context, "Unable to open local persistent data", Toast.LENGTH_LONG)
                        .show();
            }
        }
        return instance;
    }

    public boolean hasValidToken(){
        return token != null;
    }

    public static void saveData(Context context) {
        if (instance == null) return;

        PersistentData pData = new PersistentData(instance);
        try {
            FileOutputStream fos = context.openFileOutput(
                    PersistentData.PERTISTENT_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pData);
            os.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(context, "Unable to save local persistent data", Toast.LENGTH_LONG)
                    .show();
        }
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

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
