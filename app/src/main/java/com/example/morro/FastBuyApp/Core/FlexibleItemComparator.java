package com.example.morro.FastBuyApp.Core;

import java.util.Comparator;

/**
 * Used in the being project for sorting the elements out of an HashSet or HashMap data structure.
 */
class FlexibleItemComparator implements Comparator<Item> {

    private String sortingBy = "name";  //which field to sort to.

    /**
     * Comparing two object's fields
     * @param item1 first item to be compared
     * @param item2 second item to be compared with
     * @return the inequality
     */
    @Override
    public int compare(Item item1, Item item2) {
        switch(sortingBy) {
            case "name": return item1.getItemName().compareTo(item2.getItemName());
            case "id": return item1.getItemID().compareTo(item2.getItemID());
        }
        throw new RuntimeException("Error with sorting");
    }

    /** In case we want to sort by different fields at runtime */
    public void setSortingBy(String sortBy) {
        this.sortingBy = sortingBy;
    }
}

