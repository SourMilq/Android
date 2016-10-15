package com.sourmilq.sourmilq.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sourmilq.sourmilq.Fragments.PantryItemsFragment;
import com.sourmilq.sourmilq.Fragments.GroceryListItemsFragment;

import java.util.ArrayList;

/**
 * Created by Philip on 2016-10-15.
 */

public class ItemTabsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    public ItemTabsPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(new GroceryListItemsFragment());
        names.add("Grocery List"); // TODO make this a string resource

        fragments.add(new PantryItemsFragment());
        names.add("Pantry"); // TODO make this a string resource
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
