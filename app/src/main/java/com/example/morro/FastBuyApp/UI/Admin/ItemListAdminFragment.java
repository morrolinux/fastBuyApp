package com.example.morro.FastBuyApp.UI.Admin;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.ItemListFragment;

import java.util.ArrayList;

/**
 * ItemListFragment child's class which specializes for buyer side operations
 * NB: the Item(s) ArrayList is being catched from the parent class
 */
public class ItemListAdminFragment extends ItemListFragment {

    private ItemListAdminListener listener;

    /**
     * HERE we will get the ItemListAdminListener interface object for callback
     * comunication with the activity which instantiated this Fragment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ItemListAdminListener) context;
    }

    /**
     * Sync whatever changes might have happened in the store
     * to the RecyclerView's Adapter for displaying
     */
    @Override
    public void onResume(){
        super.onResume();
        setItems(listener.onItemListRequest());     //obtain the store items from the main activity
    }

    /**
     * Implementation of abstract method, as User and Admin side can have different window titles
     */
    @Override
    protected void updateWindowTitle(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Product Management");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Admin mode");
    }

    /**
     * Implementation of abstract method, as User and Admin side can have different FAB
     */
    @Override
    protected void setupFAB() {
        /* FLOATING ACTION BUTTON Instantiation */
        FloatingActionButton fab = (FloatingActionButton) super.getMainView().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    listener.onAdminFAB();
            }
        });
    }

    /** Load admin-specific menu in addition to the parent one */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_admin, menu);
    }

    /** Actions to be performend when a menu item is clicked */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_load_db){
            listener.onLoadDB();
        }else if(id == R.id.action_save_db){
            listener.onSaveDB();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * SELECTED ITEM action (from ItemAdapterListener)
             * (Let the Activity know the user have picked an object from the list)
     */
    @Override
    public void onItemSelected(Item item) {
        listener.onAdminSelected(item);
    }

    /**
     * SWIPED ITEM action (from ItemAdapterListener)
     */
    @Override
    public void onItemSwiped(Item item) {
        listener.onAdminSwiped(item);
    }

    /**
     * The activity which will implement this interface, will receive callbacks from the being fragment.
     */
    public interface ItemListAdminListener {
        void onAdminSelected(Item item);
        void onAdminSwiped(Item item);
        void onAdminFAB();
        ArrayList<Item> onItemListRequest();
        void onLoadDB();
        void onSaveDB();
    }

    /**
     * Factory method for passing a serializable object to the current Fragment during its creation.
     * Since the default constructor for a Fragment must be empty, newInstance will take care of
     * receiving the serializable object.
     */
    public static ItemListAdminFragment newInstance(ItemListAdminListener listener, ArrayList<Item> items) {
        ItemListAdminFragment fragment = new ItemListAdminFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS, items);
        fragment.setArguments(args);
        return fragment;
    }

}
