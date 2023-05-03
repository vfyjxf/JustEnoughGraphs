package com.github.vfyjxf.justenoughgraphs.api;

import com.github.vfyjxf.justenoughgraphs.api.content.*;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;

import java.util.function.BiFunction;

public interface IRegistration {

    /**
     * @param contentType the content type
     * @param helper      the content helper
     * @param renderer    the content renderer
     * @param <T>         the type of the content
     */
    <T> void registerContent(
            ContentType<T> contentType,
            IContentFactory<T> factory,
            IContentHelper<T> helper,
            IContentRenderer<T> renderer
    );

    /**
     * @param <STACK>   the stack type of the tag.
     * @param <VALUE>   the value type of the tag.
     * @param stackType the stack type of tag.For Item tag, it is ItemStack.
     * @param factory   the tag factory
     * @param renderer  the content renderer
     */
    <STACK, VALUE> void registerTagContent(
            ContentType<STACK> stackType,
            ContentType<VALUE> valueType,
            ITagFactory<STACK, VALUE> factory,
            IContentRenderer<STACK> renderer
    );

    <T> void registerNumericalContent(
            ContentType<T> contentType,
            BiFunction<T, Float, INumericalContent<T>> factory,
            IContentHelper<INumericalContent<T>> helper,
            IContentRenderer<INumericalContent<T>> renderer
    );

    void registerRecipeLooker(IRecipeLooker looker);

    <T> void registerRecipeParser(RecipeType<T> recipeType, IRecipeParser<T> parser);

    void registerUniversalRecipeParser(IUniversalRecipeParser parser);

}
