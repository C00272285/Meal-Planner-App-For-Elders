package com.example.design;

import com.google.gson.annotations.SerializedName;

public class ExtendedIngredient {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private double amount;
    @SerializedName("unit")
    private String unit;
    @SerializedName("price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}