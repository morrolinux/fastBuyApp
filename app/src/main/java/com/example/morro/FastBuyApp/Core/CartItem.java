package com.example.morro.FastBuyApp.Core;

/**
 * This class contains pairs of Item : Quantity situated into the client's cart.
 * It is just used for displaying an Item in the cartFragment
 * Also provides methods for populating the UI's ReciclerView with useful informations
 * NB: We are using a generic type T but restricting T to Item's childs type of Classes
 * This way, we can still call Item's related specific functions from getTotalPrice() for example.
 */
public class CartItem<T extends Item> {

    private T item;
    private int quantity;

    /** public constructor */
    public CartItem(T item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * @return the Item instance in the cart (not the quantity)
     */
    public T getItem() {
        return item;
    }

    /**
     * @return the Item's quantity in the cart
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Since items being added to the cart are grouped by type,
     * @return the total price for each kind of item collected in the cart.
     */
    public double getTotalPrice(){
        double p=0;
        double itemPrice = item.getItemPrice();
        p +=  itemPrice * quantity;

        /* If Item promo is 3x2, calculate the right price for it, using the quantity. */
        if(item.getItemPromo().equals("3x2")){
            int savedItems = quantity/3;
            p -= itemPrice*savedItems;
        }else if(item instanceof OffItem){      //If the item has a % discount, get its value
            int discount = ((OffItem) item).getItemDiscount();
            p -= (p * discount)/100 ;
        }

        return p;
    }

}
