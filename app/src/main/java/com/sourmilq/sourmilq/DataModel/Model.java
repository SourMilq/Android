package com.sourmilq.sourmilq.DataModel;

import android.content.Context;
import android.util.Log;

import com.sourmilq.sourmilq.Tasks.AddDeleteItem;
import com.sourmilq.sourmilq.Tasks.GetItem;
import com.sourmilq.sourmilq.Utilities.NetworkUtil;

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
    public enum ActionType {ADD, UPDATE, DELETE, GETLIST}
    public boolean isTaskRunning;

    private static final String className = "Model";
    private static Model instance = null;

    private ArrayList<Item> groceryItems;
    private ArrayList<Item> pantryItems;
    private long groceryListId;
    private long pantryListId;
    private String token;
    private Context context;

    private ArrayList<ServerTask> taskQueue;

    private Model() {
        groceryItems = new ArrayList<>();
        pantryItems = new ArrayList<>();
        taskQueue = new ArrayList<>();
        isTaskRunning = false;
    }

    public static Model getInstance(Context context) {
        if (instance == null) {
            instance = new Model();
            instance.context = context;
            try {
                FileInputStream fis = context.openFileInput(PersistentData.PERTISTENT_DATA_FILENAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                PersistentData pData = (PersistentData) is.readObject();
                is.close();
                fis.close();
                if (pData.groceryItems == null) instance.groceryItems = new ArrayList<>();
                else instance.setGroceryItems(pData.groceryItems);
                if (pData.pantryItems == null) instance.pantryItems = new ArrayList<>();
                else instance.setPantryItems(pData.pantryItems);
                instance.setGroceryListId(pData.groceryListId);
                instance.setPantryListId(pData.pantryListId);
                instance.setToken(pData.token);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
//                Toast.makeText(context, "Unable to open local persistent data", Toast.LENGTH_LONG)
//                        .show();
            }
        }
        return instance;
    }

    public boolean hasValidToken() {
        return token != null;
    }

    public static void saveData() {
        if (instance == null) return;

        PersistentData pData = new PersistentData(instance);
        try {
            FileOutputStream fos = instance.context.openFileOutput(
                    PersistentData.PERTISTENT_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pData);
            os.close();
            fos.close();
            Log.e(className, "saved data");
        } catch (IOException e) {
            Log.e(className, "couldn't save data");
            e.printStackTrace();
        }
    }

    public ArrayList<Item> getGroceryItems() {
        return groceryItems;
    }

    public void updateGroceryList() {
        ServerTask serverTask = new ServerTask(ActionType.GETLIST);
        serverTask.listid = groceryListId;
        taskQueue.add(serverTask);
        dequeueTasks();
    }

    public void setGroceryItems(ArrayList<Item> groceryItems) {
        if (groceryItems != null) {
            this.groceryItems = groceryItems;
            Log.e("blah", "overridden");
            setChanged();
            notifyObservers();
            saveData();
        }
    }

    public ArrayList<Item> getPantryItems() {
        return pantryItems;
    }

    public void setPantryItems(ArrayList<Item> pantryItems) {
        this.pantryItems = pantryItems;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        saveData();
    }

    public Long getGroceryListId() {
        return groceryListId;
    }

    public void setListIds(long id) {
        if (NetworkUtil.isConnected(context)) {
            groceryListId = id;
            updateGroceryList();
        }
    }

    public void addItem(Item item) {
        ServerTask serverTask = new ServerTask(ActionType.ADD);
        serverTask.item = item;
        serverTask.listid = groceryListId;
        taskQueue.add(serverTask);
    }

    public void deleteItem(Item item) {
        ServerTask serverTask = new ServerTask(ActionType.DELETE);
        serverTask.item = item;
        serverTask.listid = groceryListId;
        taskQueue.add(serverTask);
    }

    public void setGroceryListId(long groceryListId) {
        this.groceryListId = groceryListId;
    }

    public long getPantryListId() {
        return pantryListId;
    }

    public void setPantryListId(long pantryListId) {
        this.pantryListId = pantryListId;
    }

    ///////////////////QUEUE METHODS//////////////////////////

    public void finishedTasks(){
        isTaskRunning= false;
    }

    public void dequeueTasks() {
        if(taskQueue.isEmpty()){
            isTaskRunning = false;
            updateItems();
            return;
        }
        if (NetworkUtil.isConnected(context) && !isTaskRunning) {
            isTaskRunning = true;
            ServerTask serverTask = taskQueue.remove(0);
            switch (serverTask.actionType) {
                case ADD:
                case DELETE:
                    addDeleteItemTask(serverTask);
                    break;
                case GETLIST:
                    updateItems();
                    break;
                case UPDATE:
                    break;
            }
        }
    }

    private void addDeleteItemTask(ServerTask serverTask) {
        AddDeleteItem addDeleteItem = new AddDeleteItem(serverTask.actionType, serverTask.listid, serverTask.item, token, this);
        addDeleteItem.execute();
    }

    private void updateItems(){
        GetItem getItem = new GetItem(this);
        getItem.execute();
        saveData();
    }

}
