package com.example.morro.FastBuyApp.UI.Buyer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.morro.FastBuyApp.Core.Cart;
import com.example.morro.FastBuyApp.Core.CartItem;
import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.MainActivity;

/**
 * Instantiate Cart here; use interface methods to interact with it from within the fragments
 */
public class BuyerMainActivity extends MainActivity
        implements ItemListBuyerFragment.ItemListBuyerListener,
        ItemDetailsBuyerFragment.ItemDetailsBuyerListener, CartBuyerFragment.CartBuyerListener, CheckoutFragment.CheckoutListener {

    private Cart cart;

    /**
     * This method gets called when the Activity is created
     * @param savedInstanceState objects that can be passed upon activity creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.drawer_layout_buyer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);
        super.onCreate(savedInstanceState);     //the parent method needs the drawer to be set-up
        cart = new Cart();
    }

    /**
     * Prepare everything needed (cart and store) and start Item List Fragment
     */
    @Override
    public void loadMainFragment() {

        // pass this as the listener as we will handle callbacks from here
        ItemListBuyerFragment itemListBuyerFragment = ItemListBuyerFragment.newInstance(store.getList());

        /* Replace our fragment into layout's fragment container */
        replaceFragment(R.id.fragment_container, itemListBuyerFragment);
    }


    /**
     * Load the cart fragment for the buyer
     */
    private void loadCartFragment() {
        CartBuyerFragment cartBuyerFragment = CartBuyerFragment.newInstance(cart.getList());
        replaceFragment(R.id.fragment_container, cartBuyerFragment);
    }

    /**
     * Loads the checkout fragment for user payment
     */
    private void loadCheckoutFragment(){
        CheckoutFragment checkoutFragment = CheckoutFragment.newInstance();
        replaceFragment(R.id.fragment_container,checkoutFragment);
    }

    /**
     * show details on a product by loading DetailsFragment
     */
    private void loadDetailsFragment(Item item){
        Fragment itemDetailsBuyerFragment = ItemDetailsBuyerFragment.newInstance(item);
        replaceFragment(R.id.fragment_container, itemDetailsBuyerFragment);
    }

    /**
     * Interface implementation for ItemListBuyerFragment
     * Gets called when the user clicks on an item in the RecyclerView's list
     * @param item - the selected item to be viewed in detail and added to cart
     */
    @Override
    public void onBuyerSelected(Item item){
        loadDetailsFragment(item);
    }

    /**
     * Same as for @onBuyerSelected, but gets called when the user swipe an element from the list
     * @param item
     */
    @Override
    public void onBuyerSwiped(Item item) {
        cart.add(item,1);
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBuyerGoToCart() {
        loadCartFragment();
    }

    /**
     * ItemDetailsBuyerListener interface implementation.
     * Gets called when the user press "Add to cart" button from within the ItemDetails Buyer Fragment
     * @param item - item to be added to the cart
     * @param qty - amount of the choosen item to be added
     */
    @Override
    public void onAddToCart(Item item,int qty) {
        cart.add(item,qty);
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
    }

    /** Factory method */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}

    /**
     * Gets called when the user swipe an element from the cart
     * @param cartItem - the swiped item
     */
    @Override
    public void onCartSwiped(CartItem cartItem) {
        cart.remove(cartItem.getItem());
    }

    /**
     * Gets called when the user choses to empty its cart
     */
    @Override
    public void onEmptyCart() {
        cart.emptyCart();
    }

    /**
     * Gets called when the user pays and checks out
     */
    @Override
    public void onCheckout() {
        onEmptyCart();
        userMessage("Checkout Completed","Your cart is now empty! " +
                "(and your wallet might be as well :) ");
    }

    /**
     * CartBuyerListener method implementation
     * @return the grand total of the user cart
     */
    @Override
    public double onGetTotal() {
        return cart.getGrandTotal();
    }

    @Override
    public void onClientCheckout() {
        loadCheckoutFragment();
    }

    /**
     * Gets called when a navigation element is choosen from the navigation drawer
     * @param item : selected MenuItem
     * @return true if an element has been choosen
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Here we can call abstract methods which will be implemented in childs
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_store) {
            loadMainFragment();
        } else if (id == R.id.nav_cart) {
            loadCartFragment();
        } else if (id == R.id.nav_checkout) {
            loadCheckoutFragment();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Alerts the user with a custom message
     * @param message to be delivered to the user
     */
    private void userMessage(String title, String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                System.out.println("Pressed Ok");
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
