package com.example.design;

public class ShoppingListItem {
    private String item;
    private String aisle;
    private boolean parse;

    public ShoppingListItem(String item, String aisle, boolean parse) {
        this.item = item;
        this.aisle = aisle;
        this.parse = parse;
    }

    // Getters and setters
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public boolean isParse() {
        return parse;
    }

    public void setParse(boolean parse) {
        this.parse = parse;
    }
}
