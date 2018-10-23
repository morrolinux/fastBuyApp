package com.example.morro.FastBuyApp.UI.Buyer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morro.FastBuyApp.Core.CartItem;
import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.SwipeAndDragHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Adapter for the CartItem(s) object
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>
        implements SwipeAndDragHelper.ActionCompletionContract {

    private ArrayList<CartItem> cartItemList;
    private CartItemAdapterListener listener;

    /** Constructor */
    public CartItemAdapter(ArrayList<CartItem> itemList, CartItemAdapterListener listener){
        this.cartItemList = itemList;
        this.listener = listener;
    }

    /** ViewHolder class for interfacing data structure with the view */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView brandTextView;
        private TextView promoTextView;
        private TextView priceTextView;
        private TextView quantityTextView;
        private ImageView imageView;

        ViewHolder(View cartItemView) {
            super(cartItemView);
            nameTextView = (TextView) cartItemView.findViewById(R.id.product_name);
            brandTextView = (TextView) cartItemView.findViewById(R.id.product_brand);
            promoTextView = (TextView) cartItemView.findViewById(R.id.product_promo);
            priceTextView = (TextView) cartItemView.findViewById(R.id.product_price);
            quantityTextView = (TextView)cartItemView.findViewById(R.id.product_quantity);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            cartItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemSelected(cartItemList.get(getAdapterPosition()));
                }
            });
        }
    }

    /** Factory ViewHolder instantiation */
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.cart_item_row, parent, false);
        // Return a new holder instance
        CartItemAdapter.ViewHolder viewHolder = new CartItemAdapter.ViewHolder(productView);
        return viewHolder;
    }

    /** Gets called when the ViewHolder is being bind to the adapter, links the two */
    @Override
    public void onBindViewHolder(CartItemAdapter.ViewHolder viewHolder, int position) {
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
        CartItem cartItem = cartItemList.get(position);

        viewHolder.nameTextView.setText(cartItem.getItem().getItemName());
        viewHolder.brandTextView.setText(cartItem.getItem().getItemBrand());
        viewHolder.promoTextView.setText(cartItem.getItem().getItemPromo());
        viewHolder.quantityTextView.setText("x "+cartItem.getQuantity());
        viewHolder.priceTextView.setText(REAL_FORMATTER.format(cartItem.getTotalPrice()));
        if(cartItem.getItem().getItemImage().contains("/"))
            viewHolder.imageView.setImageURI(Uri.parse(cartItem.getItem().getItemImage()));

    }

    /**
     * Self explanatory method
     * @return number of items in the adapter
     */
    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    /**
     * Gets called when (if enabled) a view object is moved (e.g: the moving an item up or down the list)
     * @param oldPosition really the old position (where it was)
     * @param newPosition where it is now
     */
    @Override
    public void onViewMoved(int oldPosition, int newPosition) {}

    /**
     * What to do when the view element is being swiped out of the list
     * @param position of the swiped element
     */
    @Override
    public void onViewSwiped(int position) {
        listener.onItemSwiped(cartItemList.get(position));      //callback through the interface
        cartItemList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Useful for (e.g.) empty-ing the cart
     */
    public void onDeleteAll(){
        cartItemList.clear();
        notifyDataSetChanged();
    }

    /**
     * Interface for this class (for callback communication)
     */
    public interface CartItemAdapterListener {
        void onItemSelected(CartItem item);
        void onItemSwiped(CartItem item);
    }
}
