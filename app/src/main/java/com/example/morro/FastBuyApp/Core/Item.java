package com.example.morro.FastBuyApp.Core;

import java.io.Serializable;

/**
 * data model displayed by the RecyclerView
 * This is the generic product class definition
 */
public abstract class Item implements Serializable{
    private String itemName;
    private String itemBrand;
    private String itemImage;
    private String itemID;
    private String itemCategory;
    private double itemPrice;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    /** Child classes will either return discount, promo or null depending on their kind */
    public abstract String getItemPromo();

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    /** constructor */
    Item(String itemName, String itemBrand, String itemDescription, String itemID, String itemCategory, double itemPrice) {
        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.itemImage = itemDescription;
        this.itemID = itemID;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
    }

    /** Make sure each instance of this class is not being duplicated in HashMaps and HashSet(s) */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    /** Factory method used for comparing objects */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = itemName != null ? itemName.hashCode() : 0;
        result = 31 * result + (itemBrand != null ? itemBrand.hashCode() : 0);
        result = 31 * result + (itemImage != null ? itemImage.hashCode() : 0);
        result = 31 * result + (itemID != null ? itemID.hashCode() : 0);
        result = 31 * result + (itemCategory != null ? itemCategory.hashCode() : 0);
        temp = Double.doubleToLongBits(itemPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
