package com.sourmilq.sourmilq.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Item {
    private String name;
    private int numItems;
    private Double price;
    private long id;

    public Item(String name) {
        this(name, 1, 0.00, 0l);
    }

    public Item(String name, int numItems, Double price, Long id) {
        this.name = name;
        this.numItems = numItems;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("quantity",numItems);
            jsonObject.put("price",price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
