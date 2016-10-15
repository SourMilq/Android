package com.sourmilq.sourmilq.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sourmilq.sourmilq.Fragments.PantryItemsFragment;
import com.sourmilq.sourmilq.Fragments.GroceryListItemsFragment;

/**
 * Created by Philip on 2016-10-15.
 */

public class ItemTabsPagerAdapter extends FragmentPagerAdapter {

    public ItemTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GroceryListItemsFragment();
            case 1:
                return new PantryItemsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Grocery list";
            case 1:
                return "Pantry";
            default:
                return null;
        }
    }
}
