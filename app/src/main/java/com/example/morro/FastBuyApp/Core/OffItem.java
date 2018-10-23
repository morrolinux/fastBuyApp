package com.example.morro.FastBuyApp.Core;

/**
 * This class is being used for %off items, providing adequate personalization for this kind of Items
 */
public class OffItem extends Item {
    private int discount;

    /** public constructor */
    public OffItem(String itemName, String itemBrand, String itemImage, String itemID,
                   String itemCategory, int itemDiscount, double itemPrice) {
        super(itemName, itemBrand, itemImage, itemID, itemCategory, itemPrice);
        setItemDiscount(itemDiscount);
    }

    /** Method for safely setting discount percentage within range */
    private void setItemDiscount(int discount){
        if(discount > 0 && discount < 100)
            this.discount = discount;
        else
            this.discount = 1;     //safe value
    }

    /**
     * @return the item's promo string
     */
    @Override
    public String getItemPromo() {
        return discount+"%";
    }

    /**
     * @return the percentage amount to be subtracted from the total price
     */
    public int getItemDiscount(){
        return discount;
    }

}
