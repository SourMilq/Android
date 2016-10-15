package com.sourmilq.sourmilq.DataModel;

/**
 * Created by ajanthan on 16-10-15.
 */
public class Item {
    private String name;
    private int numItems;
    private Double price;

    public Item(String name) {
        this.name = name;
        this.numItems = 1;
        this.price = 0.00;
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
}
