package com.sourmilq.sourmilq.DataModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sourmilq.sourmilq.Utilities.AddDeleteItem;
import com.sourmilq.sourmilq.Utilities.GetItem;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public void updateGroceryList(){
        GetItem getItem = new GetItem();
        getItem.execute();
    }

    public void setGroceryItems(ArrayList<Item> groceryItems) {
        this.groceryItems = groceryItems;
        setChanged();
        notifyObservers();
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
        updateGroceryList();
    }

    public void addItem(Item item,onCallCompleted listener){
        AddDeleteItem addDeleteItem = new AddDeleteItem(AddDeleteItem.ActionType.ADD,groceryListId, item, listener);
        addDeleteItem.execute();
    }

    public void deleteItem(Item item, onCallCompleted listener){
        AddDeleteItem addDeleteItem = new AddDeleteItem(AddDeleteItem.ActionType.DELETE,groceryListId, item, listener);
        addDeleteItem.execute();
    }
}
