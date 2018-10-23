package com.example.morro.FastBuyApp.UI;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;

import java.util.ArrayList;

/**
 * Abstract class with RecyclerView and Search capabilities
 * This class will be extended to and specialized to delivery
 * Client side operations and Administrator side management, by simply adding and overriding methods
 */
public abstract class ItemListFragment extends Fragment implements ItemAdapter.ItemAdapterListener {
    protected static final String ITEMS = "items";

    private ArrayList<Item> items;
    private ItemAdapter itemAdapter;
    private View view = null;

    /**
     * onCreateView gets called when the Fragment is instantiated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Inflate the desired layout first */
        view = inflater.inflate(R.layout.activity_products, container, false);

        /* Set window title and subtitle */
        updateWindowTitle();

        /* Tell the fragment there are menu items */
        setHasOptionsMenu(true);

        setupFAB();

        /* the recyclerView will contain all the products */
        recyclerViewSetup();

        return view;
    }

    /**
     * TOOLBAR MENU initialization - adds search options which will be needed in both client mode
     * and Admin mode, but not in client's cart Fragment (from which it won't be called via super)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        /* SEARCH BUTTON Configuration */
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                itemAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                itemAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    /**
     * action on TOOLBAR menu item selected (Search options)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks
        int id = item.getItemId();
        checkToggle(item);

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_search_by_name){
            if(item.isChecked())
                itemAdapter.setSearchByName(true);
            else
                itemAdapter.setSearchByName(false);

            return true;
        }

        if (id == R.id.action_search_by_brand){
            if(item.isChecked())
                itemAdapter.setSearchByBrand(true);
            else
                itemAdapter.setSearchByBrand(false);
            return true;
        }

        if (id == R.id.action_search_by_code){
            if(item.isChecked())
                itemAdapter.setSearchByID(true);
            else
                itemAdapter.setSearchByID(false);
            return true;
        }

        if (id == R.id.action_filter_by_category){
            if(item.isChecked())
                itemAdapter.setSearchByCategory(true);
            else
                itemAdapter.setSearchByCategory(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Selected/Swiped Item action (from ItemAdapterListener)
     * We implement those methods so that CartBuyerFragment (which is a sligtly different class)
     * can benefit from the hierarchy without having to (unnecessarily) implement them.
     * Also, by doing this, we are basically putting no restrictions on wether the childs have to
     * implement those methods or not (whis is not really "mandatory")
     */
    @Override
    public void onItemSelected(Item item){}
    @Override
    public void onItemSwiped(Item item){}

    /** Abstarct method for setting the window title depending on which fragment we are on */
    protected abstract void updateWindowTitle();

    /** Abstract method for setting up Floating Action Button accordingly to which fragment we're on */
    protected abstract void setupFAB();

    /** RECYCLERVIEW Instantiation is left to the parent as it's the same for both admin and user */
    protected void recyclerViewSetup(){

        /* Lookup the recyclerview in activity layout */
        RecyclerView rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        /* Create adapter passing in the data */
        itemAdapter = new ItemAdapter(this.getContext(), items,this);
        /* Attach the adapter to the recyclerview to populate items */
        rvItems.setAdapter(itemAdapter);
        /* Set layout manager to position the items */
        rvItems.setLayoutManager(new LinearLayoutManager(this.getContext()));
        /* add swype and drag gestures */
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rvItems);
    }

    /**
     * Useful for operations on view
     * @return the current view
     */
    protected View getMainView(){
        return view;
    }

    /**
     * Gets called upon Fragment instantiaton.
     * Here we get the list of Item(s) to be displayed (same for both Client/Admin sides)
     * @param savedInstanceState - a bundle of objects that can be passed upon creation
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (ArrayList<Item>) getArguments().getSerializable(ITEMS);
        }
    }

    /** is checkbox checked or not? */
    private void checkToggle(MenuItem item){
        if(item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
    }

    /**
     * Method used to load new data to be displayed into the Adapter
     * public so that I can call it from any activity which inflated this fragment
     * @param items ArrayList of items for populating the Adapter
     */
    public void setItems(ArrayList<Item> items){
        this.items.clear();
        this.items.addAll(items);
        itemAdapter.notifyDataSetChanged();
    }

}
