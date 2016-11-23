package com.sourmilq.sourmilq.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sourmilq.sourmilq.Adapters.ExpireListAdapter;
import com.sourmilq.sourmilq.Adapters.PantryItemListAdapter;
import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.ItemsActivity;
import com.sourmilq.sourmilq.R;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PantryItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PantryItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PantryItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PantryItemListAdapter mAdapter;

    private LayoutInflater mInflater;

    public PantryItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PantryItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PantryItemsFragment newInstance(String param1, String param2) {
        PantryItemsFragment fragment = new PantryItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mInflater = inflater;

        View view = inflater.inflate(R.layout.fragment_pantry_items, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PantryItemListAdapter(getActivity().getApplicationContext(), view, this);
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPantryItemsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPantryItemsFragmentInteraction(Uri uri);
    }

    public void showPantryItemEditDialog(final int position) {
        final Item item = mAdapter.getDataset().get(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Actions for " + item.getName());

        String[] options = {"Edit Expiration", "Delete", "Cancel"};

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Item updatedItem;
                int updatedPosition;
                ArrayList<Item> dataset = mAdapter.getDataset();
                ITEM_SEARCH:
                {
                    for (updatedPosition = 0; updatedPosition < dataset.size(); updatedPosition++) {
                        updatedItem = dataset.get(updatedPosition);
                        if (updatedItem.equals(item)) {
                            break ITEM_SEARCH;
                        }
                    }
                    // item not found
                    Toast.makeText(getContext(), "Error: can't find item", Toast.LENGTH_SHORT);
                    dialog.cancel();
                    return;
                }
                switch (which) {
                    case 0: // edit expiration
                        // calendar dialog
//                        Calendar expiration = new GregorianCalendar();
//
//                        dataset.get(updatedPosition).setExpiration(expiration);
//                        mAdapter.notifyItemChanged(updatedPosition);
//                        mAdapter.getModel().setExpiration(item, expiration);
                        DialogFragment expFragment = new ExpirationPickerFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("item", updatedItem);
                        expFragment.setArguments(args);
                        expFragment.show(getActivity().getFragmentManager(), "expirationDatePicker");
                        break;
                    case 1: // delete
                        mAdapter.remove(updatedPosition);
                        mAdapter.getModel().deletePantryItem(updatedItem);
                        break;
                    case 2:
                        dialog.cancel();
                        break;
                }
            }
        });

        alertDialog.show();
    }

    public void showPantryWarnExpireDialog(ArrayList<Item> expired, ArrayList<Item> soon) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Warning");

        final View expirationWarningAlertView = mInflater
                .inflate(R.layout.alert_expiration_warning, null);
        final TextView expiredText = (TextView) expirationWarningAlertView
                .findViewById(R.id.expiredText);
        final RecyclerView expiredItemsView = (RecyclerView) expirationWarningAlertView
                .findViewById(R.id.expiredItemsView);
        final TextView expireSoonText = (TextView) expirationWarningAlertView
                .findViewById(R.id.expireSoonText);
        final RecyclerView expireSoonItemsView = (RecyclerView) expirationWarningAlertView
                .findViewById(R.id.expireSoonItemsView);

        if (expired.isEmpty()) {
            expiredText.setVisibility(View.INVISIBLE);
            expiredItemsView.setVisibility(View.INVISIBLE);
        } else {
            // use a linear layout manager
            RecyclerView.LayoutManager RLayoutManager = new LinearLayoutManager(getActivity());
            expiredItemsView.setLayoutManager(RLayoutManager);

            // specify an adapter (see also next example)
            ExpireListAdapter adapter = new ExpireListAdapter(expired);
            expiredItemsView.setAdapter(adapter);
        }

        if (soon.isEmpty()) {
            expireSoonText.setVisibility(View.INVISIBLE);
            expireSoonItemsView.setVisibility(View.INVISIBLE);
        } else {
            // use a linear layout manager
            RecyclerView.LayoutManager RLayoutManager = new LinearLayoutManager(getActivity());
            expireSoonItemsView.setLayoutManager(RLayoutManager);

            // specify an adapter (see also next example)
            ExpireListAdapter adapter = new ExpireListAdapter(soon);
            expireSoonItemsView.setAdapter(adapter);
        }

        alertDialog.setView(expirationWarningAlertView);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void editExpiration(Item item, Calendar date) {
        Item updatedItem;
        int updatedPosition;
        ArrayList<Item> dataset = mAdapter.getDataset();
        ITEM_SEARCH:
        {
            for (updatedPosition = 0; updatedPosition < dataset.size(); updatedPosition++) {
                updatedItem = dataset.get(updatedPosition);
                if (updatedItem.equals(item)) {
                    break ITEM_SEARCH;
                }
            }
            // item not found
            Toast.makeText(getContext(), "Error: can't find item", Toast.LENGTH_SHORT);
            return;
        }

        mAdapter.remove(updatedPosition);
        mAdapter.getModel().setExpiration(updatedItem, date);
    }
}
