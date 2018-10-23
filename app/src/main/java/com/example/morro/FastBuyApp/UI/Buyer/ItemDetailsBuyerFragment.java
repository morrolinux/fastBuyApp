package com.example.morro.FastBuyApp.UI.Buyer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.ItemDetailsFragment;

/**
 * Fragment for displaying details of a passed product. user-side specialization
 */
public class ItemDetailsBuyerFragment extends ItemDetailsFragment implements View.OnClickListener {
    private Item item;
    private ItemDetailsBuyerListener listener;
    private Spinner quantitySpinner;

    /**
     * catch the listener (will be the Activity which instantiated the Fragment)
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (ItemDetailsBuyerListener) context;
    }

    /**
     * get the Item object to display in details
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = super.getItem();     // Retrive the item object
    }

    /**
     * gets called when the view is being created.
     * Here we will hinibit edit functions for client side operations
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        // Inflating user-specific UI elements
        inflater.inflate(R.layout.user_controls,((ViewGroup)view.findViewById(R.id.extraLayout)));
        quantitySpinner = view.findViewById(R.id.spinner_quantity);
        view.findViewById(R.id.buyButton).setOnClickListener(this);

        // Disable all EditText from being editable in client/user mode
        editTextDisable(view,R.id.product_name);
        editTextDisable(view,R.id.product_brand);
        editTextDisable(view,R.id.product_promo);
        editTextDisable(view,R.id.product_category);
        editTextDisable(view,R.id.product_id);
        editTextDisable(view,R.id.product_price);

        quantitySpinnerSetup();

        return view;
    }

    /**
     * setup the spinner object to have values from 1 to 100
     * (will be used as quantity to be added to the cart)
     */
    private void quantitySpinnerSetup(){
        Integer[] qtyArray = new Integer[100];
        for (int i = 0; i < 100; ++i) qtyArray[i] = i+1;
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),android.R.layout.simple_spinner_item, qtyArray);
        quantitySpinner.setAdapter(adapter);
    }

    /** useful function for setting a UI EditText field to non-editable */
    private void editTextDisable(View view, @IdRes int element){
        view.findViewById(element).setFocusable(false);
        view.findViewById(element).setClickable(false);
    }

    /**
     * Implementation of the OnClickListener (for the buy button and many others if needed)
     * @param view the view item to be checked
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buyButton:
                listener.onAddToCart(item, (Integer) quantitySpinner.getSelectedItem());
                break;
            default:
                break;
        }
    }

    /** interface definition for callbacks */
    public interface ItemDetailsBuyerListener {
        void onAddToCart(Item item,int qty);
    }

    /** Factory method to create a new instance of this fragment using the provided parameters. */
    public static ItemDetailsBuyerFragment newInstance(Item item) {
        Bundle args = new Bundle();
        args.putSerializable(ITEM,item);
        ItemDetailsBuyerFragment fragment = new ItemDetailsBuyerFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
