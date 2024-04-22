package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PriceBreakdownResponse {
    @SerializedName("ingredients")
    private List<ExtendedIngredient> ingredients;
    @SerializedName("totalCost")
    private double totalCost;
    @SerializedName("totalCostPerServing")
    private double totalCostPerServing;

    public List<ExtendedIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ExtendedIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalCostPerServing() {
        return totalCostPerServing;
    }

    public void setTotalCostPerServing(double totalCostPerServing) {
        this.totalCostPerServing = totalCostPerServing;
    }
}
