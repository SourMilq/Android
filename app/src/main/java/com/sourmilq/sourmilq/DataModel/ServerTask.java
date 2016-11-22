package com.sourmilq.sourmilq.DataModel;

/**
 * Created by ajanthan on 2016-11-17.
 */

public class ServerTask {
    public Model.ActionType actionType;
    public Item item;
    public long listid;
    public Recipe recipe;

    public ServerTask(Model.ActionType actionType){
           this.actionType = actionType;
    }
}
