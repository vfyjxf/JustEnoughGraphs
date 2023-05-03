package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IngredientType;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class RecipeHelper {

    public static <T> mezz.jei.api.recipe.RecipeType<T> toJeiType(RecipeType<T> recipeType) {
        ResourceLocation id = recipeType.getId();
        return mezz.jei.api.recipe.RecipeType.create(id.getNamespace(), id.getPath(), recipeType.getRecipeClass());
    }

    public static <T> RecipeType<T> toJeghType(mezz.jei.api.recipe.RecipeType<T> recipeType) {
        ResourceLocation id = recipeType.getUid();
        return RecipeType.create(id.getNamespace(), id.getPath(), recipeType.getRecipeClass());
    }

    public static <T> Map<IngredientType, List<IContent<?>>> parseRecipe(T recipe) {
        IRegistryManager registryManager = IRegistryManager.getInstance();
        Optional<IRecipeParser<T>> maybeParser = registryManager.getRecipeParser(recipe);
        Map<IngredientType, List<IContent<?>>> ingredients = new HashMap<>(IngredientType.values().length);
        maybeParser.ifPresentOrElse(
                parser -> ingredients.putAll(parser.parse(recipe)),
                () -> parseByUniversal(registryManager, recipe, ingredients)
        );
        return ingredients;
    }

    private static <T> void parseByUniversal(IRegistryManager registryManager, T recipe, Map<IngredientType, List<IContent<?>>> ingredients) {
        RecipeType<T> recipeType = registryManager.getRecipeType(recipe);
        IUniversalRecipeParser recipeParser = registryManager.getUniversalRecipeParsers()
                .stream()
                .filter(parser -> parser.isSupport(recipeType))
                .findFirst()
                .orElse(null);
        if (recipeParser != null) {
            var all = recipeParser.parse(recipe);
            ingredients.putAll(all);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Map<RecipeType<?>, List<?>> lookupRecipe(IContent<T> content) {
        return IRegistryManager.getInstance()
                .getRecipeLookers()
                .stream()
                .map(looker -> looker.lookForRecipes(content))
                .reduce(new HashMap<>(), (map1, map2) -> {
                    map2.forEach((type, recipes) -> map1.computeIfAbsent(type, t -> new ArrayList<>())
                            .addAll((List) recipes));
                    return map1;
                });
    }

}
