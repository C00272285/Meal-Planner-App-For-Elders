package com.example.design;

public class BMRCalculator{

    // Method to calculate estimated total amount of calories using Mifflin-St Jeor Equation
    public static double calculateBMR(String gender, double weight, double height, int age, String activityLevel) {
        double bmr;

        if ("Male".equalsIgnoreCase(gender)) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else { // Female
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        return adjustForActivityLevel(bmr, activityLevel);
    }

    private static double adjustForActivityLevel(double bmr, String activityLevel) {
        switch (activityLevel) {
            case "Sedentary":
                return bmr * 1.2;
            case "Lightly active":
                return bmr * 1.375;
            case "Moderately active":
                return bmr * 1.55;
            case "Very active":
                return bmr * 1.725;
            case "Super active":
                return bmr * 1.9;
            default:
                return bmr;
        }
    }
}