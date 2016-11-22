package com.sourmilq.sourmilq.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Fragments.PantryItemsFragment;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private PantryItemsFragment fragment;

    public PantryItemListAdapter(Context context, View containerView, PantryItemsFragment fragment) {
        model = Model.getInstance(context);
        mDataset = new ArrayList<>();
        mDataset.clear();
        for (Item item : model.getPantryItems()) {
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
        ArrayList<Item> updatedDataset = model.getPantryItems();

        // only notify changes if changes exist (makes UI look better)
        COMPARE_NEW:
        {
            if (mDataset.size() != updatedDataset.size()) break COMPARE_NEW;
            int size = mDataset.size();
            for (int i = 0; i < size; i++) {
//                int d1 = mDataset.get(i).getExpiration().get(Calendar.DAY_OF_MONTH);
//                int d2 = updatedDataset.get(i).getExpiration().get(Calendar.DAY_OF_MONTH);
                if (!(mDataset.get(i).equals(updatedDataset.get(i))) ||
                        !mDataset.get(i).sameDate(updatedDataset.get(i)))
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
    public PantryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pantry_list_item, parent, false);
        ViewHolder vh = new ViewHolder(this, view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mDataset.get(position);
        String expString = "null";
        if (item.getExpiration() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            expString = sdf.format(item.getExpiration().getTime());
        }
        holder.mTextView.setText(item.getName() + "(" + item.getNumItems() + ")" + expString);
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

    public void showPantryItemEditDialog(int position) {
        fragment.showPantryItemEditDialog(position);
    }

    public Model getModel() {
        return model;
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
            mAdapter.showPantryItemEditDialog(position);
            return true;
        }
    }
}
