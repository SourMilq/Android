package com.sourmilq.sourmilq.Adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Philip on 2016-10-15.
 */

public class GroceryItemListAdapter extends RecyclerView.Adapter<GroceryItemListAdapter.ViewHolder> implements Observer {
    private onCallCompleted listener;
    private ArrayList<Item> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView mTextView;
        public GroceryItemListAdapter mAdapter;

        public ViewHolder(GroceryItemListAdapter adapter, View v) {
            super(v);
            mAdapter = adapter;
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mTextView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getLayoutPosition();
//            mAdapter.remove(position);

            Model.getInstance().deleteItem(mAdapter.getDataset().get(position), mAdapter.getListener());
            return true;
        }
    }

    @Override
    public void update(Observable observable, Object data) {
//        Model model = Model.class.cast(observable);
//        for (Item item : model.getGroceryItems()) {
//            mDataset.add(item.getName());
//        }
        mDataset = Model.getInstance().getGroceryItems();
        notifyDataSetChanged();
    }

    public GroceryItemListAdapter(onCallCompleted listener) {
        this.listener = listener;
        mDataset = new ArrayList<>();
        Model model = Model.getInstance();
        model.addObserver(this);
//        update(model, null);
    }

    @Override
    public GroceryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grocery_list_item, parent, false);
        ViewHolder vh = new ViewHolder(this, view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

//    public void remove(int position) {
//        mDataset.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public void add(String newItem) {
//        mDataset.add(new Item(newItem));
//        notifyDataSetChanged();
//    }

    public ArrayList<Item> getDataset() {
        return mDataset;
    }

    public onCallCompleted getListener() {
        return listener;
    }
}
