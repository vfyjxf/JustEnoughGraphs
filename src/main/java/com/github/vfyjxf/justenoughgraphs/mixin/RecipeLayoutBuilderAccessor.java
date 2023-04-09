package com.github.vfyjxf.justenoughgraphs.mixin;

import mezz.jei.library.gui.recipes.RecipeLayoutBuilder;
import mezz.jei.library.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(RecipeLayoutBuilder.class)
public interface RecipeLayoutBuilderAccessor {
    @Invoker
    Stream<IRecipeLayoutSlotSource> callSlotStream();
}
