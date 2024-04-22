package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShoppingListRecipeDetail {
    @SerializedName("extendedIngredients")
    private List<ExtendedIngredient> extendedIngredients;
    public List<ExtendedIngredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public static class ExtendedIngredient {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("amount")
        private double amount;
        @SerializedName("unit")
        private String unit;
        @SerializedName("cost")
        private double price;


        public int getId() { return id; }
        public String getName() { return name; }
        public double getAmount() { return amount; }
        public String getUnit() { return unit; }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAmount(double amount) {
            this.amount = amount;
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
}