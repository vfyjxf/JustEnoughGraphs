package com.github.vfyjxf.justenoughgraphs.api;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.IDescriptiveContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public interface IRegistration {

    <T> void registerContent(ContentType<T> contentType, IContentHelper<T> helper, IContentRenderer<T> renderer);

    <V, T extends IDescriptiveContent<V>> void registerDescriptive(
            ResourceLocation identifier,
            IContentHelper<T> helper,
            IContentRenderer<T> renderer,
            IDescriptiveContent.IFactory<T> factory
    );

    void registerRecipeLooker(IRecipeLooker looker);

    <T> void registerRecipeParser(RecipeType<T> recipeType, IRecipeParser<T> parser);

    void registerUniversalRecipeParser(IUniversalRecipeParser parser);

}
