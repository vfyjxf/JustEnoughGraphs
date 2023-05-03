package com.github.vfyjxf.justenoughgraphs.api.recipe;

import mezz.jei.api.recipe.RecipeIngredientRole;

public enum IngredientType {

    /**
     * The ingredient is used as input such energy and item.
     */
    INPUT,
    /**
     * The ingredient is used as output.
     */
    OUTPUT,
    /**
     * The ingredient is used as catalyst. It contains machine.
     */
    CATALYST,
    /**
     * The ingredient is used as condition.
     */
    OTHER;

    public static RecipeIngredientRole toJei(IngredientType type) {
        return switch (type) {
            case INPUT -> RecipeIngredientRole.INPUT;
            case OUTPUT -> RecipeIngredientRole.OUTPUT;
            case CATALYST -> RecipeIngredientRole.CATALYST;
            default -> RecipeIngredientRole.RENDER_ONLY;
        };
    }

    public static IngredientType fromJei(RecipeIngredientRole role) {
        return switch (role) {
            case INPUT -> INPUT;
            case OUTPUT -> OUTPUT;
            case CATALYST -> CATALYST;
            default -> OTHER;
        };
    }

}
