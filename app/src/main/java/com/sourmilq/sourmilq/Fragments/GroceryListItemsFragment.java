package com.sourmilq.sourmilq.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.android.gms.plus.PlusOneButton;
import com.sourmilq.sourmilq.Adapters.GroceryItemListAdapter;
import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.callBacks.SimpleItemTouchHelperCallback;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link GroceryListItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroceryListItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceryListItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private GroceryItemListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Model model;

    public GroceryListItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroceryListItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroceryListItemsFragment newInstance(String param1, String param2) {
        GroceryListItemsFragment fragment = new GroceryListItemsFragment();
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
        this.model = Model.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grocery_list_items, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GroceryItemListAdapter(getActivity().getApplicationContext(), view);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Add Item");

                final View newGroceryItemDialogueView = inflater
                        .inflate(R.layout.dialogue_new_grocery_item, null);
                final EditText editNameView = (EditText) newGroceryItemDialogueView
                        .findViewById(R.id.editName);
                final NumberPicker quantityPickerView = (NumberPicker) newGroceryItemDialogueView
                        .findViewById(R.id.quantityPicker);
                quantityPickerView.setMinValue(1);
                quantityPickerView.setMaxValue(50);
                quantityPickerView.setValue(1);
                final EditText editPriceView = (EditText) newGroceryItemDialogueView
                        .findViewById(R.id.editPrice);

                alertDialog.setView(newGroceryItemDialogueView);

                alertDialog.setPositiveButton("Add Item",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                mAdapter.add(input.getText().toString());
                                String name = editNameView.getText().toString();
                                int quantity = quantityPickerView.getValue();
                                String priceAsString = editPriceView.getText().toString();
                                double price = priceAsString.isEmpty() ? 0.00 : Double.parseDouble(priceAsString);
                                long id = 0l;
                                Item newItem = new Item(name, quantity, price, id);
                                mAdapter.add(newItem);
                                model.addItem(newItem);
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        FloatingActionButton updateButton = (FloatingActionButton) view.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.updateList();
                Snackbar.make(v, "Updating...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
//        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGroceryListItemsFragmentInteraction(uri);
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
        void onGroceryListItemsFragmentInteraction(Uri uri);
    }

}
