package com.github.vfyjxf.justenoughgraphs.api.recipe;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * A helper for looking up recipes.
 */
public interface IRecipeLooker {

    <T> boolean isSupport(RecipeType<T> recipeType);

    /**
     * Look for recipes that can produce the given result.
     */
    <T> Map<RecipeType<?>, Collection<?>> lookForRecipes(IContent<T> result);

    /**
     * Returns a possible recipe of any type
     */
    default <T> Optional<?> lookForRecipe(IContent<T> result) {
        return lookForRecipes(result)
                .values()
                .stream()
                .findAny()
                .stream()
                .flatMap(Collection::stream)
                .findAny();
    }

    <T> Map<RecipeType<?>, Collection<?>> lookForRecipesByInput(IContent<T> input);

    default <T> Optional<?> lookForRecipeByInput(IContent<T> input) {
        return lookForRecipesByInput(input)
                .values()
                .stream()
                .findAny()
                .stream()
                .flatMap(Collection::stream)
                .findAny();
    }


}
