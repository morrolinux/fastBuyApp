package com.example.morro.FastBuyApp.UI.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.Core.OffItem;
import com.example.morro.FastBuyApp.Core.PromoItem;
import com.example.morro.FastBuyApp.Core.StandardItem;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.ItemDetailsFragment;

/**
 * Fragment for displaying details of a passed product. user-side specialization
 */
public class ItemDetailsAdminFragment extends ItemDetailsFragment implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    public static final int OPERATION_INSERT = 0;
    public static final int OPERATION_EDIT = 1;
    private static final String OPERATION = "requestedOperation";
    private int requestedOperation;
    private Item item;
    private ItemDetailsAdminListener listener;
    private String itemImage = "";

    private EditText product_name;
    private EditText product_brand;
    private EditText product_promo;
    private EditText product_category;
    private EditText product_id;
    private EditText product_price;
    private ImageView imageView;

    /**
     * Gets called when the Fragment is attaching to the container
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (ItemDetailsAdminListener) context;
    }

    /**
     * Gets called when the Fragment is created
     * @param savedInstanceState bundle of objects which can be passed upon creation
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        item = super.getItem();             // Retrive the item object
        itemImage = item.getItemImage();    // Retrive its image for edits
        if (getArguments() != null) {
            requestedOperation = (int) getArguments().getSerializable(OPERATION);
        }
    }

    /**
     * gets called when the view is being created.
     * Here we will hinibit edit functions for client side operations
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        // Inflating admin-specific UI elements
        inflater.inflate(R.layout.admin_controls,((ViewGroup)view.findViewById(R.id.extraLayout)));

        view.findViewById(R.id.pushProductButton).setOnClickListener(this);
        view.findViewById(R.id.deleteButton).setOnClickListener(this);

        product_name = view.findViewById(R.id.product_name);
        product_brand = view.findViewById(R.id.product_brand);
        product_promo = view.findViewById(R.id.product_promo);
        product_category = view.findViewById(R.id.product_category);
        product_id = view.findViewById(R.id.product_id);
        product_price = view.findViewById(R.id.product_price);
        imageView = view.findViewById(R.id.imageView);

        imageView.setOnClickListener(this);

        return view;
    }

    /**
     * Implementation of the OnClickListener (for the buy button and many others if needed)
     * @param view the view item to be checked
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pushProductButton:
                pushProduct();
                getActivity().onBackPressed();
                break;
            case R.id.deleteButton:
                listener.onDeleteItem(item);
                getActivity().onBackPressed();
                break;
            case R.id.imageView:
                pickImageFromStorage();
                break;
            default:
                break;
        }
    }

    /**
     * Open the gallery for picking up an image
     */
    private void pickImageFromStorage(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Apply the image once the open file activity has returned
     * @param requestCode which operation was the activity started for
     * @param resultCode return code of the activity
     * @param resultData data returned by the activity. from which we gain the URI
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            Uri sourceTreeUri = resultData.getData();
            getContext().getContentResolver().takePersistableUriPermission(sourceTreeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            itemImage = String.valueOf(resultData.getData());
            imageView.setImageURI(Uri.parse(itemImage));     //real-time update the imageView displaying the product
        }
    }

    /**
     * Either create a new product or modify the existing one, as requested by requestedOperation.
     * NB: HashMap and HashSet data structures can find an object based on its proprieties.
     * Since modified objects are totally different form previous the edit to the hash function,
     * pushing a modified object without deleting the existing one first, will result in both
     * the old and the new objects present in the HashMap/HashSet, which is not what we want.
     */
    private void pushProduct(){
        if(requestedOperation == OPERATION_EDIT){
            listener.onDeleteItem(item);
            listener.onInsertItem(createItemFromView());
        }else if(requestedOperation == OPERATION_INSERT){
            listener.onInsertItem(createItemFromView());
        }
    }

    /**
     * parse the UI fields and generate an Item instance with all the requested data
     * @return generated Item
     */
    private Item createItemFromView(){
        Item tmp;
        String name = product_name.getText().toString();
        String brand = product_brand.getText().toString();
        String promo = product_promo.getText().toString();
        String category = product_category.getText().toString();
        String id = product_id.getText().toString();
        double price = Double.parseDouble(product_price.getText().toString());

        if(promo.equals("3x2")){
            tmp = new PromoItem(name,brand,itemImage,id,category,promo,price);
        }else if(promo.contains("%")){
            int discount = Integer.parseInt(promo.substring(0,promo.indexOf("%")));
            tmp = new OffItem(name,brand,itemImage,id,category,discount,price);
        }else{
            tmp = new StandardItem(name,brand,itemImage,id,category,price);
        }
        return tmp;
    }

    /** interface definition for callbacks */
    public interface ItemDetailsAdminListener {
        void onInsertItem(Item item);
        void onDeleteItem(Item item);
    }

    /** Factory method to create a new instance of this fragment using the provided parameters. */
    public static ItemDetailsAdminFragment newInstance(Item item, int requestedOperation) {
        Bundle args = new Bundle();
        args.putSerializable(ITEM,item);
        args.putSerializable(OPERATION,requestedOperation);
        ItemDetailsAdminFragment fragment = new ItemDetailsAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
