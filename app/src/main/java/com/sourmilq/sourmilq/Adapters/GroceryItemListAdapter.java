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
import com.sourmilq.sourmilq.Fragments.GroceryListItemsFragment;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Philip on 2016-10-15.
 */

public class GroceryItemListAdapter extends RecyclerView.Adapter<GroceryItemListAdapter.ViewHolder> implements Observer, ItemTouchHelperAdapter {
    private onCallCompleted listener;
    private ArrayList<Item> mDataset;
    private Model model;
    private View containerView;
    private GroceryListItemsFragment fragment;

    public GroceryItemListAdapter(Context context, View containerView, GroceryListItemsFragment fragment) {
        model = Model.getInstance(context);
        mDataset = new ArrayList<>();
        mDataset.clear();
        for (Item item : model.getGroceryItems()) {
            mDataset.add(new Item(item));
        }
        notifyDataSetChanged();
        model.addObserver(this);
//        update(model, null);
        this.containerView = containerView;
        this.fragment = fragment;
    }

    @Override
    public void update(Observable observable, Object data) {
        fragment.setDoneUpdating();

        ArrayList<Item> updatedDataset = model.getGroceryItems();

        // only notify changes if changes exist (makes UI look better)
        COMPARE_NEW:
        {
            if (mDataset.size() != updatedDataset.size()) break COMPARE_NEW;
            int size = mDataset.size();
            for (int i = 0; i < size; i++) {
                if (!(mDataset.get(i).equals(updatedDataset.get(i))))
                    break COMPARE_NEW;
            }
            return;
        }
        mDataset.clear();
        for (Item item : updatedDataset) {
            mDataset.add(new Item(item));
        }
        notifyDataSetChanged();
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

    @Override
    public void onItemDismiss(int position) {
        Item itemToRemove = mDataset.get(position);
        remove(position);
        model.checkOffItem(itemToRemove);
        Snackbar.make(
                containerView,
                "Moved " + itemToRemove.getName() + " to pantry...",
                Snackbar.LENGTH_LONG
        ).show();
    }

    public ArrayList<Item> getDataset() {
        return mDataset;
    }

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
            Item item = mAdapter.getDataset().get(position);
            mAdapter.remove(position);
            mAdapter.model.deleteGroceryItem(item);
            return true;
        }
    }
}
