package com.sourmilq.sourmilq.DataModel;

import android.content.Context;
import android.util.Log;

import com.sourmilq.sourmilq.Tasks.AddDeleteItem;
import com.sourmilq.sourmilq.Tasks.CheckOffItem;
import com.sourmilq.sourmilq.Tasks.GetItem;
import com.sourmilq.sourmilq.Tasks.SetExpirationItem;
import com.sourmilq.sourmilq.Utilities.NetworkUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Model extends Observable {
    public enum ActionType {ADD, DONE, DELETE, GETLIST, UPDATE}

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

    public void updateList() {
        ServerTask serverTask = new ServerTask(ActionType.GETLIST);
        taskQueue.add(serverTask);
        dequeueTasks();
    }

    public void setGroceryItems(ArrayList<Item> groceryItems) {
        if (groceryItems != null) {
            this.groceryItems = groceryItems;
            applyChanges();
        }
    }

    public ArrayList<Item> getPantryItems() {
        return pantryItems;
    }

    public void setPantryItems(ArrayList<Item> pantryItems) {
        if (pantryItems != null) {
            this.pantryItems = pantryItems;
            applyChanges();
        }
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

    public void setListIds(ArrayList<Long> ids) {
        if (NetworkUtil.isConnected(context)) {
            groceryListId = ids.get(0);
            pantryListId = ids.get(1);
            updateItems();
        }
    }

    public void addItem(Item item) {
        ServerTask serverTask = new ServerTask(ActionType.ADD);
        serverTask.item = item;
        serverTask.listid = groceryListId;
        taskQueue.add(serverTask);
        dequeueTasks();

        groceryItems.add(item);
        applyChanges();
    }

    private void deleteItem(Item item, long listID) {
        ServerTask serverTask = new ServerTask(ActionType.DELETE);
        serverTask.item = item;
        serverTask.listid = listID;
        taskQueue.add(serverTask);
        dequeueTasks();
    }

    public void deleteGroceryItem(Item item) {
        for (Item i : groceryItems) {
            if (item.equals(i)) {
                groceryItems.remove(i);
                break;
            }
        }
        applyChanges();
        deleteItem(item, groceryListId);
    }

    public void deletePantryItem(Item item) {
        for (Item i : pantryItems) {
            if (item.equals(i)) {
                pantryItems.remove(i);
                break;
            }
        }
        applyChanges();
        deleteItem(item, pantryListId);
    }

    public void checkOffItem(Item item) {
        ServerTask serverTask = new ServerTask(ActionType.DONE);
        serverTask.item = item;
        taskQueue.add(serverTask);
        dequeueTasks();

        for (int i = 0; i < groceryItems.size(); i++) {
            if (item.equals(groceryItems.get(i))) {
                groceryItems.remove(i);
                break;
            }
        }
        pantryItems.add(item);
        applyChanges();
    }

    public void setExpiration(Item item, Calendar date) {
        Item updatedItem;
        for (int i = 0; i < pantryItems.size(); i++) {
            updatedItem = pantryItems.get(i);
            if (item.equals(updatedItem)) {
                updatedItem.setExpiration(date);
                applyChanges();
                break;
            }
        }

        ServerTask serverTask = new ServerTask(ActionType.UPDATE);
        serverTask.item = item;
        serverTask.listid = pantryListId;
        taskQueue.add(serverTask);
        dequeueTasks();

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

    private void applyChanges() {
        setChanged();
        notifyObservers();
        saveData();
    }

    ///////////////////QUEUE METHODS//////////////////////////

    public void finishedTasks() {
        isTaskRunning = false;
    }

    public void dequeueTasks() {

        if (taskQueue.isEmpty()) {
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
                case DONE:
                    checkOffItemTask(serverTask);
                    break;
                case UPDATE:
                    setExpirationTask(serverTask);
                    break;
            }
        }
    }

    private void setExpirationTask(ServerTask serverTask) {
        SetExpirationItem setExpirationItem = new SetExpirationItem(serverTask.listid,serverTask.item,token,this);
        setExpirationItem.execute();
    }

    private void addDeleteItemTask(ServerTask serverTask) {
        AddDeleteItem addDeleteItem = new AddDeleteItem(serverTask.actionType, serverTask.listid, serverTask.item, token, this);
        addDeleteItem.execute();
    }

    private void updateItems() {
        new GetItem(this, pantryListId).execute();
        new GetItem(this, groceryListId).execute();
        saveData();
    }

    private void checkOffItemTask(ServerTask serverTask) {
        CheckOffItem checkOffItem = new CheckOffItem(groceryListId, serverTask.item, token, this);
        checkOffItem.execute();
    }

}
