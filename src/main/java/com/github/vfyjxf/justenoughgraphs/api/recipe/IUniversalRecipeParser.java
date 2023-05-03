package com.github.vfyjxf.justenoughgraphs.api.recipe;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IUniversalRecipeParser extends IRecipeParser {

    <T> boolean isSupport(RecipeType<T> recipeType);

    @Override
    Map<IngredientType, List<IContent<?>>> parse(Object recipe);

    @Override
    Collection<IContent<?>> parseByType(Object recipe, IngredientType type);
}
