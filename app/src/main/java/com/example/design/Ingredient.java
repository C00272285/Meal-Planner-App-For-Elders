package com.example.design;

import com.google.gson.annotations.SerializedName;

public class Ingredient
{
    // to get the name, amount and unit of each ingredient
    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private double amount;

    @SerializedName("unit")
    private String unit;




    public String getName()
    {
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
