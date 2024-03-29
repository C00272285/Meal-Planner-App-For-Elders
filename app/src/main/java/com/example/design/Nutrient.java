package com.example.design;

import com.google.gson.annotations.SerializedName;

public class Nutrient
{
    //the name of the food from the JSON file
    @SerializedName("name")
    String name;

    //the amount of the food item from the JSON file
    @SerializedName("amount")
    double amount;

    //the unit of measurement of the food from the JSON file
    @SerializedName("unit")
    String unit;

    //getters and setters for the variables
    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }


}
