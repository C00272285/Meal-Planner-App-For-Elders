package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NutritionData
{
    //get the nutrients form the JSON file that has the matching name "nutrients".
    @SerializedName("nutrients")
    List<Nutrient> nutrients;

    //getters and setters for the variables
    public List<Nutrient> getNutrients()
    {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients)
    {
        this.nutrients = nutrients;
    }

}
