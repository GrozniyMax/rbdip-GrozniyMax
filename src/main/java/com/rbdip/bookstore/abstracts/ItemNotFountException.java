package com.rbdip.bookstore.abstracts;

public class ItemNotFountException extends IllegalArgumentException {
    public <T> ItemNotFountException(String itemName, T itemId) {
        super(itemName + " " + itemId+ " not found");
    }
}
