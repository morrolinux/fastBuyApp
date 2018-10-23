package com.example.morro.FastBuyApp.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will hold the elements the user is going to buy, and provide basic operations on them
 */
public class Cart {
    private HashMap<Item,Integer> cartProducts = new HashMap<>();

    /** add a product. if the object already exists, increment its quantity */
    public void add(Item item,int quantity){
        if(cartProducts.containsKey(item)){
            Integer qty = cartProducts.get(item);
            qty += quantity;
            cartProducts.put(item,qty); //overwriting the existing element
        }else{
            cartProducts.put(item,quantity);
        }
    }

    /** completely remove the passed object from the list */
    public void remove(Item item){
        if(cartProducts.containsKey(item)){
            cartProducts.remove(item);
        }
    }

    /** lower the quantity of the passed object */
    public void remove(Item item,int quantity){
        if(cartProducts.containsKey(item)){
            Integer qty = cartProducts.get(item);
            qty -= quantity;
            if(qty <= 0){
                remove(item);
            }
        }
    }

    /** empty the cart by removing all objects in cartProducts HashMap */
    public void emptyCart(){
        cartProducts.clear();
    }

    /** Calculate the grand total of the cart */
    public double getGrandTotal(){
        double total = 0;
        for(CartItem cartItem: getList()){     // generate arraylist of CartItems and parse it
            total += cartItem.getTotalPrice();
        }
         return total;
    }

    /**
     * This Method returns all the Items in the cart, grouped by type, and their quantity.
     * It's useful for the RecyclerView's ViewHolder to quickly display the total for each kind of item(s)
     * And for the Cart itself for calculating the grand total.
     * @return an ArrayList of CartItems, which contains the pair Item : quantity
     */
    public ArrayList<CartItem> getList(){
        ArrayList<CartItem> list = new ArrayList<CartItem> ();
        for(Map.Entry<Item, Integer> entry : cartProducts.entrySet()){
            list.add(new CartItem<Item>(entry.getKey(),entry.getValue()) );
        }
        return list;
    }


}
