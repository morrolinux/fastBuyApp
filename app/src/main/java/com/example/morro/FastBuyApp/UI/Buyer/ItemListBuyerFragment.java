package com.example.morro.FastBuyApp.UI.Buyer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.ItemListFragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ItemListFragment child's class which specializes for buyer side operations
 * NB: the Item(s) ArrayList is being catched from the parent class
 */
public class ItemListBuyerFragment extends ItemListFragment {

    private ItemListBuyerListener listener;

    /**
     * HERE we will get the ItemListBuyerListener interface object for callback
     * comunication with the activity which instantiated this Fragment
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (ItemListBuyerListener) context;
    }

    /**
     * Implementation of abstract method, as User and Admin side can have different window titles
     */
    @Override
    protected void updateWindowTitle(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Shopping");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Client mode");
    }

    /**
     * Implementation of abstract method, as User and Admin side can have different FAB
     */
    @Override
    protected void setupFAB() {
        /* FLOATING ACTION BUTTON Instantiation */
        FloatingActionButton fab = (FloatingActionButton) super.getMainView().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBuyerGoToCart();
            }
        });
    }

    /**
     * SELECTED ITEM action (from ItemAdapterListener)
     * (Let the Activity know the user have picked an object from the list)
     */
    @Override
    public void onItemSelected(Item item) {
        listener.onBuyerSelected(item);
    }

    /**
     * SWIPED ITEM action (from ItemAdapterListener)
     */
    @Override
    public void onItemSwiped(Item item) {
        listener.onBuyerSwiped(item);
    }

    /**
     * The activity which will implement this interface, will receive callbacks from the being fragment.
     */
    public interface ItemListBuyerListener{
        void onBuyerSelected(Item item);
        void onBuyerSwiped(Item item);
        void onBuyerGoToCart();
    }

    /**
     * Factory method for passing a serializable object to the current Fragment during its creation.
     * Since the default constructor for a Fragment must be empty, newInstance will take care of
     * receiving the serializable object.
     */
    public static ItemListBuyerFragment newInstance(ArrayList<Item> items) {
        ItemListBuyerFragment fragment = new ItemListBuyerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS,(Serializable) items);
        fragment.setArguments(args);
        return fragment;
    }

}
