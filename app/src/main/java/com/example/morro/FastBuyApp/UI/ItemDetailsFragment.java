package com.example.morro.FastBuyApp.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;

import java.text.DecimalFormat;

/**
 * Fragment for viewing details of a choosen item.
 */
public abstract class ItemDetailsFragment extends Fragment {

    private DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
    protected static final String ITEM = "item";
    private Item item;

    /** Retrive the passed item */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (Item) getArguments().getSerializable(ITEM);
        }
    }

    /** Here we link the item's data fields to the view for displaying */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);

        // Set All the views according to the selected item
        ((EditText)view.findViewById(R.id.product_name)).setText(item.getItemName());
        ((EditText)view.findViewById(R.id.product_brand)).setText(item.getItemBrand());
        ((EditText)view.findViewById(R.id.product_promo)).setText(item.getItemPromo());
        ((EditText)view.findViewById(R.id.product_category)).setText(item.getItemCategory());
        ((EditText)view.findViewById(R.id.product_id)).setText(item.getItemID());
        ((EditText)view.findViewById(R.id.product_price)).setText(REAL_FORMATTER.format(item.getItemPrice()));
        if(item.getItemImage().contains("/")) {
            ((ImageView) view.findViewById(R.id.imageView)).setImageURI(Uri.parse(item.getItemImage()));
        }

        return view;
    }

    /**
     * @return the current viewing item.
     */
    public Item getItem() {
        return item;
    }

}
