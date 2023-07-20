package com.wgu.software1.Data;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.wgu.software1.Data.*;
import javafx.collections.ObservableListBase;

/**
 * written Inventory.java
 */

/**
 *
 * @author Nabeel Aref
 */
public class Inventory {
//RUNTIME ERROR
    //These lines where initially initialize assigned to FXCollections.emptyObservableList() which in return
    //did not alllow any object to be put in these two ObservableLists
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts= FXCollections.observableArrayList();

    public static void addPart( Part newPart)
    {
        allParts.add(newPart);
    }
    public static void addProduct( Product newProduct)
    {
        allProducts.add(newProduct);
    }
    public static Part lookupPart( int partId)
    {
        for (Part part: allParts) {
            if(part.getId() == partId)
            {
                return part;
            }
        }
        return null;
    }
    public static Product lookupProduct( int productId){
        for (Product product: allProducts) {
            if(product.getId() == productId)
            {
                return product;
            }
        }
        return null;
    }
    public static ObservableList<Part> lookupPart( String partName){
        ObservableList<Part> partsFound = FXCollections.emptyObservableList();
        for (Part part: allParts) {
            if(part.getName().contains(partName))
            {
                partsFound.add(part);
            }
        }
        return partsFound;
    }
    public static ObservableList<Product> lookupProduct( String productName){
        ObservableList<Product> productsFound = FXCollections.emptyObservableList();
        for (Product product: allProducts) {
            if(product.getName().contains(productName))
            {
                productsFound.add(product);
            }
        }
        return productsFound;
    }
    public static void updatePart( int index,  Part selectedPart){

        for (Part part: allParts) {
            if(part.getId() == selectedPart.getId()) {
                allParts.remove(part);
                if ((index > allParts.size())) {
                    allParts.add(selectedPart);
                } else {
                    allParts.add(index, selectedPart);
                }
            }
        }
    }
    public static void updateProduct( int index,  Product newProduct){
        for (Product product: allProducts) {
            if(product.getId() == newProduct.getId()) {
                allProducts.remove(product);
                if ((index > allProducts.size())) {
                    allProducts.add(newProduct);
                } else {
                    allProducts.add(index, newProduct);
                }
            }
        }
    }
    public static boolean deletePart( Part selectedPart){
        for (Part part: allParts) {
            if(part.getId() == selectedPart.getId())
            {
                allParts.remove(part);
                return true;
            }
        }
        return false;
    }
    public static boolean deleteProduct( Product selectedProduct){
        for (Product product: allProducts) {
            if(product.getId() == selectedProduct.getId())
            {
                allProducts.remove(product);
                return true;
            }
        }
        return false;
    }
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
