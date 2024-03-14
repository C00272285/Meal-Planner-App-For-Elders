package com.example.design;
import java.util.List;
public class AnalyzedInstruction
{
    // Hold the steps of the Meal in a list
    private final List<Step> steps;

    public AnalyzedInstruction(List<Step> steps) {
        this.steps = steps;
    }

    // List of steps
    public List<Step> getSteps()
    {
        return steps;
    }


    public static class Step
    {
        private final int number;
        private final String step;

        public Step(int number, String step)
        {
            this.number = number;
            this.step = step;
        }

        // Getter for step number
        public int getNumber()
        {
            return number;
        }

        // Getter for instruction text
        public String getStep()
        {
            return step;
        }

    }

}
