package com.sourmilq.sourmilq.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class PantryItemListAdapter extends RecyclerView.Adapter<PantryItemListAdapter.ViewHolder> implements Observer {
    private onCallCompleted listener;
    private ArrayList<Item> mDataset;
    private Model model;
    private View containerView;

    public PantryItemListAdapter(Context context, View containerView) {
        model = Model.getInstance(context);
        mDataset = new ArrayList<>(model.getPantryItems());
        notifyDataSetChanged();
        model.addObserver(this);
//        update(model, null);
        this.containerView = containerView;
    }

    @Override
    public void update(Observable observable, Object data) {
        ArrayList<Item> updatedDataset = model.getPantryItems();

        // only notify changes if changes exist (makes UI look better)
        COMPARE_NEW:
        {
            if (mDataset.size() != updatedDataset.size()) break COMPARE_NEW;
            int size = mDataset.size();
            for (int i = 0; i < size; i++) {
                if (!(mDataset.get(i).equals(updatedDataset.get(i))))
                    break COMPARE_NEW;
            }
            int i = 1;
            return;
        }
        mDataset = new ArrayList<>(updatedDataset);
        notifyDataSetChanged();
    }

    @Override
    public PantryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pantry_list_item, parent, false);
        ViewHolder vh = new ViewHolder(this, view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mDataset.get(position);
        holder.mTextView.setText(item.getName() + " (" + item.getNumItems() + ")");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Item newItem) {
        mDataset.add(newItem);
        notifyItemInserted(mDataset.size() - 1);
    }

    public ArrayList<Item> getDataset() {
        return mDataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView mTextView;
        public PantryItemListAdapter mAdapter;

        public ViewHolder(PantryItemListAdapter adapter, View v) {
            super(v);
            mAdapter = adapter;
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mTextView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getLayoutPosition();
            Item item = mAdapter.getDataset().get(position);
            mAdapter.remove(position);
            mAdapter.model.deletePantryItem(item);
            return true;
        }
    }
}
