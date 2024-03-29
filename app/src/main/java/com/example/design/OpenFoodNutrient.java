package com.example.design;
public class OpenFoodNutrient
{
    String name;    // Gets the name of the Nutrient
    double amount;  // Gets the Amount
    String unit;    // Gets the Unit of Measurement

    //  Getters and Setters
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

    //  Returns a formatted string combining the amount and unit making it easier to read
    //  For example if amount is 5 and unit is "g" this method returns "5 g"
    public String getValue() {
        return String.format("%s %s", amount, unit);
    }
}