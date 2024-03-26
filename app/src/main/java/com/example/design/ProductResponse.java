package com.example.design;

public class ProductResponse
{
    public Product product; // To get the product name of the scanned food item

    public static class Product
    {
        public String code;
        public String product_name;
        public String brands;
    }
}
