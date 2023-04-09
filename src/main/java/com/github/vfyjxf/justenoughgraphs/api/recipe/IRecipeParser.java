package com.github.vfyjxf.justenoughgraphs.api.recipe;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IRecipeParser<R> {

    /**
     * Different types of ingredients in a recipe.
     * Each element in the list represents a "slow".
     */
    Map<IngredientType, List<IContent<?>>> parse(R recipe);

    Collection<IContent<?>> parseByType(R recipe, IngredientType type);

}
