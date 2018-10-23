package com.example.morro.FastBuyApp.Core;

/**
 * Extends the abstract Item class and overrides getItemPromo returning the current promo for the product.
 */
public class PromoItem extends Item {
    private String promo;

    /** public constructor */
    public PromoItem(String itemName, String itemBrand, String itemImage, String itemID,
                     String itemCategory, String itemPromo, double itemPrice) {
        super(itemName, itemBrand, itemImage, itemID, itemCategory, itemPrice);
        this.promo = itemPromo;
    }

    /**
     * @return the promo string
     */
    @Override
    public String getItemPromo() {
        return promo;
    }
}
