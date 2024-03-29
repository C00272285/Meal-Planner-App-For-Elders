package com.example.design;
public class ProductResponse
{
    // This will get the product name, brand and the nutrients from the scanned item using Open Food Facts API
    public Product product;

    public static class Product
    {
        private final String product_name;
        private final String brands;
        public Nutriments nutriments;

        public Product(String productName, String brands)
        {
            product_name = productName;
            this.brands = brands;
        }

        // Getters
        public String getProductName()
        {
            return product_name;
        }
        public String getBrands()
        {
            return brands;
        }
        public Nutriments getNutriments()
        {
            return nutriments;
        }
    }

    public static class Nutriments
    {
        public double energy, protein, sugars, fat, fiber;
        public double getEnergy()
        {
            return energy;
        }



        public void setEnergy(double energy) {
            this.energy = energy;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getSugars() {
            return sugars;
        }

        public void setSugars(double sugars) {
            this.sugars = sugars;
        }

        public double getFat() {
            return fat;
        }

        public void setFat(double fat) {
            this.fat = fat;
        }

        public double getFiber() {
            return fiber;
        }

        public void setFiber(double fiber) {
            this.fiber = fiber;
        }
    }
}
