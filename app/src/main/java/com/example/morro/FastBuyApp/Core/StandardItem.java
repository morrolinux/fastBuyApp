package com.example.morro.FastBuyApp.Core;

/**
 * Extends the abstract Item class, with no promo and no discount applied.
 */
public class StandardItem extends Item {

    /** public constructor */
    public StandardItem(String itemName, String itemBrand, String itemImage, String itemID,
                        String itemCategory, double itemPrice) {
        super(itemName, itemBrand, itemImage, itemID, itemCategory, itemPrice);
    }

    /** no promo is applied */
    @Override
    public String getItemPromo() {
        return "";
    }
}
