package com.github.vfyjxf.justenoughgraphs.mixin;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.recipes.RecipeManagerInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RecipeManagerInternal.class)
public interface RecipeManagerInternalAccessor {
    @Accessor
    List<IRecipeCategory<?>> getRecipeCategories();
}
