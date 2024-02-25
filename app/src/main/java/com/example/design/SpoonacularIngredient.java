package com.example.design;

import com.google.gson.annotations.SerializedName;

public class SpoonacularIngredient
{
    //the SerializedName has to match what is in the Json file for it to correctly parse the information.
    @SerializedName("name")
    String name;

    @SerializedName("nutrition")
    NutritionData nutrition;

    //getters and setters for the variables
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public NutritionData getNutrition()
    {
        return nutrition;
    }

    public void setNutrition(NutritionData nutrition)
    {
        this.nutrition = nutrition;
    }

}
