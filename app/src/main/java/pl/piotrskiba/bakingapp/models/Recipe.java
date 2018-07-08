package pl.piotrskiba.bakingapp.models;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void addStep(Step step) {
        steps.add(0, step);
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
