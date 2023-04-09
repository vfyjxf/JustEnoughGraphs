package com.github.vfyjxf.justenoughgraphs.api.recipe;

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
    OTHER

}
