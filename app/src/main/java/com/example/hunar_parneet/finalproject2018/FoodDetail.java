package com.example.hunar_parneet.finalproject2018;

public class FoodDetail {
    private final String food;
    private final String calories;
    private final String fatContent;

    public FoodDetail(String food, String calories, String fatContent){
        this.food=food;
        this.calories=calories;
        this.fatContent=fatContent;

    }

    public String getFood() {
        return food;
    }

    public String getCalories() {
        return calories;
    }

    public String getFatContent() {
        return fatContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodDetail that = (FoodDetail) o;

        if (!getFood().equals(that.getFood())) return false;
        if (!getCalories().equals(that.getCalories())) return false;
        return getFatContent().equals(that.getFatContent());
    }

    @Override
    public int hashCode() {
        int result = getFood().hashCode();
        result = 31 * result + getCalories().hashCode();
        result = 31 * result + getFatContent().hashCode();
        return result;
    }
}
