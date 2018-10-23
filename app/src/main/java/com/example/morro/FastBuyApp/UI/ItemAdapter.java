package com.example.morro.FastBuyApp.UI;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morro.FastBuyApp.Core.Item;
import com.example.morro.FastBuyApp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Create the basic adapter extending from RecyclerView.Adapter
 * And Specify the custom ViewHolder which gives us access to our views
 * NB: An adapter is needed to actually populate the data into the RecyclerView
 * NB: This class implements SwipeAndDragHelper.ActionCompletionContract interface to catch
 *      events on the SwipeAndDragHelper for the RecyclerView (callback)
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
        implements SwipeAndDragHelper.ActionCompletionContract, Filterable {

    private Context mContext;                   // Store the context for easy access
    private ArrayList<Item> itemList;                // Store a member variable for the items
    private ArrayList<Item> itemListFiltered;        // This list will be used for filtering results
    private ItemAdapterListener listener;       // The passed object which will implement the interface

    private boolean searchByName = true;
    private boolean searchByBrand = true;
    private boolean searchByCategory = true;
    private boolean searchByID = true;

    /** constructor */
    ItemAdapter(Context context, ArrayList<Item> items, ItemAdapterListener listener) {
        itemList = items;
        this.itemListFiltered = itemList;   // We need both cartItemList and itemListFiltered to perform search
        this.listener = listener;
        mContext = context;
    }

    /**
     * Getters/Setters for search behaviour
     */
    public void setSearchByName(boolean searchByName) {
        this.searchByName = searchByName;
    }

    public void setSearchByBrand(boolean searchByBrand) {
        this.searchByBrand = searchByBrand;
    }

    public void setSearchByCategory(boolean searchByCategory) {
        this.searchByCategory = searchByCategory;
    }

    public void setSearchByID(boolean searchByID) {
        this.searchByID = searchByID;
    }


    /**
     * ViewHolder class: provides a direct reference to each of the views within a data item
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView brandTextView;
        private TextView promoTextView;
        private TextView categoryTextView;
        private TextView idTextView;
        private TextView priceTextView;
        private ImageView imageView;

        // constructor that accepts an entire item row
        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.product_name);
            brandTextView = (TextView) itemView.findViewById(R.id.product_brand);
            promoTextView = (TextView) itemView.findViewById(R.id.product_promo);
            categoryTextView = (TextView) itemView.findViewById(R.id.product_category);
            idTextView = (TextView) itemView.findViewById(R.id.product_id);
            priceTextView = (TextView) itemView.findViewById(R.id.product_price);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send the selected item in callback
                    // We are calling onItemSelected on the class which will implement the interface
                    listener.onItemSelected(itemListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    /** Easy access to the context object */
    private Context getContext() {
        return mContext;
    }


    /**
     * Inflating a layout from XML and returning the holder
     */
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    /**
     * Populating data into the item through the holder
     */
    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int position) {
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
        // Get the data model based on position
        Item item = itemListFiltered.get(position);      //we always use ListFiltered due to search implementation

        // Set item views based on your views and data model
        viewHolder.nameTextView.setText(item.getItemName());
        viewHolder.brandTextView.setText(item.getItemBrand());
        viewHolder.promoTextView.setText(item.getItemPromo());
        viewHolder.categoryTextView.setText(item.getItemCategory());
        viewHolder.idTextView.setText(item.getItemID());
        viewHolder.priceTextView.setText(REAL_FORMATTER.format(item.getItemPrice()));
        if(item.getItemImage().contains("/"))
            viewHolder.imageView.setImageURI(Uri.parse(item.getItemImage()));
    }

    /**
     * @return the total count of items in the list
     */
    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    /**
     * handle moving gestures on the view
     */
    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Item item = itemListFiltered.get(oldPosition);
        itemListFiltered.remove(oldPosition);
        itemListFiltered.add(newPosition, item);
        notifyItemMoved(oldPosition, newPosition);
    }

    /**
     * handle swipe gestures on the view (Perform actions)
     */
    @Override
    public void onViewSwiped(final int position) {
        listener.onItemSwiped(itemListFiltered.get(position));      // CALLING interface method to capture swipe action just as for clicks
        itemListFiltered.remove(position);
        notifyItemRemoved(position);
    }


    /*
     * implement Filterable's Filter method
     */

    /**
     * provides a Filter object for filtering strings on the elements
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    ArrayList<Item> filteredList = new ArrayList<>();    // collect all results a List
                    for (Item row : itemList) {
                        if ( searchByName ) {
                            if (row.getItemName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                                continue;           //no need to add a thing multiple times
                            }
                        }
                        if( searchByBrand ){
                            if (row.getItemBrand().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                                continue;           //no need to add a thing multiple times
                            }
                        }
                        if ( searchByCategory ) {
                            if (row.getItemCategory().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                                continue;           //no need to add a thing multiple times
                            }
                        }
                        if ( searchByID ) {
                            if (row.getItemID().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                                continue;           //no need to add a thing multiple times
                            }
                        }

                    }
                    itemListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    /** Interface definition for callbacks */
    public interface ItemAdapterListener {
        void onItemSelected(Item item);
        void onItemSwiped(Item item);
    }

}