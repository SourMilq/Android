package com.sourmilq.sourmilq.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Fragments.PantryItemsFragment;
import com.sourmilq.sourmilq.ItemsActivity;
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
        fragment.setDoneUpdating();

        ArrayList<Item> updatedDataset = model.getPantryItems();

        // only notify changes if changes exist (makes UI look better)
        COMPARE_NEW:
        {
            if (mDataset.size() != updatedDataset.size()) break COMPARE_NEW;
            int size = mDataset.size();
            for (int i = 0; i < size; i++) {
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

        ItemsActivity activity = (ItemsActivity) fragment.getActivity();
        if (activity != null && !activity.expirationWarned) {
            activity.expirationWarned = true;

            ArrayList<Item> expired = new ArrayList<>();
            ArrayList<Item> soon = new ArrayList<>();

            Calendar now = Calendar.getInstance();
            Calendar later = Calendar.getInstance();
            later.add(Calendar.DATE, 2);

            for (Item item : mDataset) {
                Calendar expiration = item.getExpiration();
                if (expiration == null) continue;

                if (now.after(expiration)) {
                    expired.add(item);
                } else if (later.after(expiration)) {
                    soon.add(item);
                }
            }

            if (!expired.isEmpty() || !soon.isEmpty()) fragment.showPantryWarnExpireDialog(expired, soon);
        }
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
        String expString = "unspecified";
        if (item.getExpiration() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            expString = sdf.format(item.getExpiration().getTime());
        }
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText("Quantity: " + item.getNumItems());
        holder.tvExpiration.setText("Expires on: " + expString);
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

        public TextView tvName;
        public TextView tvQuantity;
        public TextView tvExpiration;
        public PantryItemListAdapter mAdapter;

        public ViewHolder(PantryItemListAdapter adapter, View v) {
            super(v);
            mAdapter = adapter;
            tvName = (TextView) v.findViewById(R.id.info_text_pantry);
            tvQuantity = (TextView) v.findViewById(R.id.quantity_text_pantry);
            tvExpiration = (TextView) v.findViewById(R.id.expiration_text_pantry);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getLayoutPosition();
            mAdapter.showPantryItemEditDialog(position);
            return true;
        }
    }
}
