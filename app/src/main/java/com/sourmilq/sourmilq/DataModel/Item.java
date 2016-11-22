package com.sourmilq.sourmilq.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Item implements Serializable {
    private String name;
    private int numItems;
    private Double price;
    private long id;
    private Calendar expiration;

    public Item(String name) {
        this(name, 1, 0.00, 0l);
    }

    public Item(String name, int numItems, Double price, Long id) {
          this.name = name;
           this.numItems = numItems;
            this.price = price;
         this.id = id;
    }

    public Item(Item other) {
        this.name = other.getName();
        this.numItems = other.getNumItems();
        this.price = other.getPrice();
        this.id = other.getId();
        this.expiration = other.getExpiration();
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

    public Calendar getExpiration() {
        return expiration;
    }

    public void setExpiration(Calendar date) {
        expiration = date;
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("quantity",numItems);
            jsonObject.put("price",price);
            if (expiration != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                jsonObject.put("expiration",sdf.format(expiration));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public boolean equals(Item other) {
        return name.equals(other.getName()) &&
                numItems == other.getNumItems() &&
                Math.abs(price - other.getPrice()) < 0.0049 &&
                id == other.getId();
    }

    public boolean sameDate(Item other) {
        return expiration != null && other.getExpiration() != null &&
                expiration.get(Calendar.YEAR) == other.getExpiration().get(Calendar.YEAR) &&
                expiration.get(Calendar.MONTH) == other.getExpiration().get(Calendar.MONTH) &&
                expiration.get(Calendar.DAY_OF_MONTH) == other.getExpiration().get(Calendar.DAY_OF_MONTH);
    }
}
