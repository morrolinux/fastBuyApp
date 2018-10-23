package com.example.morro.FastBuyApp.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * The store contains all the products in stock (in unlimited quantities)
 * Also handles data loading/saving from/to a DB file.
 */
public class Store implements Serializable{
    private HashSet<Item> productList;

    public static final String DEFAULT_STORE_PATH = "/Download/db.dat";

    /** public constructor instantiate the productList HashSet */
    public Store(){
        productList = new HashSet<>();
    }

    /** add a product */
    public void add(Item item){
        productList.add(item);
    }

    /** remove the passed object from the list */
    public void remove(Item item){
        if(productList.contains(item)){
            productList.remove(item);
        }
    }

    /**
     * remove all elements from the store.
     */
    public void clearDB(){
        productList.clear();
    }

    /**
     * @return an ArrayList containing all the Items present in the store
     */
    public ArrayList<Item> getList(){
        ArrayList<Item> items = new ArrayList<Item>(productList);
        FlexibleItemComparator comparator = new FlexibleItemComparator();
        Collections.sort(items,comparator);
        return items;
    }


    /**
     * save item list to file
     * @param fileName name of the file to save to
     * @return operation result (ok=1/nok=0)
     */
    public boolean saveDatabase(String fileName) {
        try {
            File file = new File(fileName);
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(this.productList);
            os.close();
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * load item list from file
     * @param fileName name of the file to load from
     * @return operation result (ok=1/nok=0)
     */
    public boolean loadDatabase(String fileName) {
        HashSet<Item> temp;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            temp = (HashSet<Item>)is.readObject();
            is.close();
            this.productList.clear();
            this.productList.addAll(temp);
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
