package com.github.vfyjxf.justenoughgraphs.mixin;

import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.gui.recipes.RecipesGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RecipesGui.class)
public interface RecipesGuiAccessor {
    @Accessor
    List<IRecipeLayoutDrawable<?>> getRecipeLayouts();
}
