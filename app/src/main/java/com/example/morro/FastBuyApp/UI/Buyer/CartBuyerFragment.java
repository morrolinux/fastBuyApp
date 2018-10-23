package com.example.morro.FastBuyApp.UI.Buyer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.morro.FastBuyApp.Core.CartItem;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.ItemListFragment;
import com.example.morro.FastBuyApp.UI.SwipeAndDragHelper;

import java.util.ArrayList;

/**
 * ItemListFragment child's class which specializes for client's cart operations.
 * Note that we have to @Override recyclerViewSetup() and few other elements since
 * the given object is a different and a different adapter is being used.
 */
public class CartBuyerFragment extends ItemListFragment implements CartItemAdapter.CartItemAdapterListener {

    private CartBuyerListener listener;
    private ArrayList<CartItem> cartItems;
    private CartItemAdapter cartItemAdapter;    //adapter for cartItem objects

    /** RECYCLERVIEW Instantiation */
    @Override
    protected void recyclerViewSetup(){
        RecyclerView rvItems = (RecyclerView) getMainView().findViewById(R.id.rvItems);
        cartItemAdapter = new CartItemAdapter(cartItems,this);
        rvItems.setAdapter(cartItemAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this.getContext()));
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(cartItemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rvItems);
    }

    /** Create a custom menu for the cart */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //not calling super since we don't want search options enabled
        inflater.inflate(R.menu.menu_cart, menu);
    }

    /**
     * Listen on events on the cart menu, callback the
     * Activity which holds the listener
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_empty_cart){
            listener.onEmptyCart();         //empty the cart (hold by the Activity)
            cartItemAdapter.onDeleteAll();  //reflect changes in the Adapter (hold by the Fragment)
            updateWindowTitle();
            return true;
        }
        return false;
    }

    /** Setting a custom title for the cart */
    @Override
    protected void updateWindowTitle(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cart");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Grand Total: "+listener.onGetTotal());
    }

    /** Setting a custom FAB for the cart */
    @Override
    protected void setupFAB() {
        /* FLOATING ACTION BUTTON Instantiation */
        FloatingActionButton fab = (FloatingActionButton) super.getMainView().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_shopping_basket_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClientCheckout();
            }
        });
    }

    /**
     * Factory method for passing a serializable object to the current Fragment during its creation.
     * Since the default constructor for a Fragment must be empty, newInstance will take care of
     * receiving the serializable object.
     */
    public static CartBuyerFragment newInstance(ArrayList<CartItem> cartItems) {
        CartBuyerFragment fragment = new CartBuyerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS, cartItems);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * HERE we will retrive the CartBuyerListener interface object for callback
     * comunication with the activity which instantiated this Fragment
     * NB: unlike the ItemListBuyer Fragment, we read the object's ArrayList here, since the
     * parent isn't qualified to manage CartItem objects
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (CartBuyerListener) context;
        if (getArguments() != null) {
            cartItems = (ArrayList<CartItem>) getArguments().getSerializable(ITEMS);
        }
    }

    /**
     * Implementing CartItemAdapterListener (interface) methods from the CartItem Adapter
     */
    @Override
    public void onItemSelected(CartItem cartItem) {}

    /**
     * Implementing CartItemAdapterListener (interface) methods from the CartItem Adapter
     */
    @Override
    public void onItemSwiped(CartItem cartItem) {
        listener.onCartSwiped(cartItem);
        updateWindowTitle();
    }

    /**
     * Interface definition for the Cart Fragment
     */
    public interface CartBuyerListener {
        void onCartSwiped(CartItem cartItem);
        void onEmptyCart();
        double onGetTotal();
        void onClientCheckout();
    }

}
