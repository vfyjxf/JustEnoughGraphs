package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class RecipeHelper {

    public static <T> mezz.jei.api.recipe.RecipeType<T> toJeiType(RecipeType<T> recipeType) {
        ResourceLocation id = recipeType.getId();
        return mezz.jei.api.recipe.RecipeType.create(id.getNamespace(), id.getPath(), recipeType.getRecipeClass());
    }

    public static <T> RecipeType<T> toJeghType(mezz.jei.api.recipe.RecipeType<T> recipeType) {
        ResourceLocation id = recipeType.getUid();
        return RecipeType.create(id.getNamespace(), id.getPath(), recipeType.getRecipeClass());
    }

}
