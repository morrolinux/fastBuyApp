package com.example.morro.FastBuyApp.UI.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.Core.StandardItem;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.LoginActivity;
import com.example.morro.FastBuyApp.UI.MainActivity;

import java.util.ArrayList;

import static com.example.morro.FastBuyApp.UI.Admin.ItemDetailsAdminFragment.OPERATION_EDIT;
import static com.example.morro.FastBuyApp.UI.Admin.ItemDetailsAdminFragment.OPERATION_INSERT;

/**
 * This activity will be used for all admin Fragments
 */
public class AdminMainActivity extends MainActivity implements ItemListAdminFragment.ItemListAdminListener,
        ItemDetailsAdminFragment.ItemDetailsAdminListener{

    private static final int OPERATION_SAVE = 0;
    private static final int OPERATION_LOAD = 1;
    private static final String DEFAULT_FOLDER = "/Download/";

    /**
     * This method gets called when the Activity is created
     * @param savedInstanceState objects that can be passed upon activity creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storeSetup();
        setContentView(R.layout.drawer_layout_admin);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads the main Fragment (which is the item list fragment)
     */
    @Override
    public void loadMainFragment() {

        // pass this as the listener as we will handle callbacks from here
        ItemListAdminFragment itemListAdminFragment = ItemListAdminFragment.newInstance(this, store.getList());

        // Replace our fragment into layout's fragment container
        replaceFragment(R.id.fragment_container, itemListAdminFragment);
    }

    /**
     * show details on a product by loading DetailsFragment
     */
    private void loadDetailsFragment(Item item, int operation){
        Fragment itemDetailsAdminFragment = ItemDetailsAdminFragment.newInstance(item, operation);
        replaceFragment(R.id.fragment_container, itemDetailsAdminFragment);
    }

    /**
     * Method for reading items database from a custom filename
     * @param fileName to load the db from
     */
    private void loadDBFromFile(String fileName){
        String root = Environment.getExternalStorageDirectory().toString()+DEFAULT_FOLDER;
        store.clearDB();
        store.loadDatabase(root +fileName);
    }

    /**
     * Method for saving items database to a custom filename
     * @param fileName to save the db to
     */
    private void saveDBToFile(String fileName){
        String root = Environment.getExternalStorageDirectory().toString()+DEFAULT_FOLDER;
        store.saveDatabase(root +fileName);
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

        if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Gets called by the item list fragment via interface, loads the item details fragment in place
     */
    @Override
    public void onAdminSelected(Item item) {
        loadDetailsFragment(item,OPERATION_EDIT);
    }

    /**
     * Gets called by the item list fragment via interface, delete the item from the store
     */
    @Override
    public void onAdminSwiped(Item item) {
        store.remove(item);
    }

    /**
     * Launches the "Add product" procedure, by making and displaying an empty dummy item as a model
     */
    @Override
    public void onAdminFAB() {
        Item sampleItem = new StandardItem("","", "","","",0);
        loadDetailsFragment(sampleItem,OPERATION_INSERT);
    }

    /**
     * Gets called from ItemListAdminFragment via interface
     * @return the objects from the store
     */
    @Override
    public ArrayList<Item> onItemListRequest() {
        return store.getList();
    }

    /**
     * Gets called from ItemDetailsAdminFragment (when delete button is pressed) via interface
     * @param item the item to be deleted
     */
    @Override
    public void onDeleteItem(Item item){
        store.remove(item);
    }

    /**
     * Gets called from ItemDetailsAdminFragment upon "push product" button bein pressed
     * @param item to be added to the store
     */
    @Override
    public void onInsertItem(Item item) {
        store.add(item);
    }

    /**
     * Gets called by ItemListAdminListener (via interface) when the admin chooses to load DB from file.
     * Provides a functional popup window for the admin to type the filename
     */
    @Override
    public void onLoadDB() {
        showOpenSaveDialog("Load DB from file",OPERATION_LOAD);
    }

    /**
     * Gets called by ItemListAdminListener (via interface) when the admin chooses to save DB from file
     * Provides a functional popup window for the admin to type the filename
     */
    @Override
    public void onSaveDB() {
        showOpenSaveDialog("Save DB to file",OPERATION_SAVE);
    }

    /**
     * Show the Dialog to let the admin input the filename to save to / load from
     */
    private void showOpenSaveDialog(String title, final int operation){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.open_save_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage("Enter file name");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            final EditText editText = (EditText) dialogView.findViewById(R.id.filename);

            public void onClick(DialogInterface dialog, int whichButton) {
                String fileName = editText.getText().toString();
                if(operation == OPERATION_LOAD) {
                    loadDBFromFile(fileName);

                    //reload the content into the displaying fragment containing the RecyclerView
                    //https://developer.android.com/training/basics/fragments/communicating
                    ItemListAdminFragment fragment = (ItemListAdminFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment.setItems(store.getList());

                }else if(operation == OPERATION_SAVE){
                    saveDBToFile(fileName);
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                System.out.println("Operation abort");
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
