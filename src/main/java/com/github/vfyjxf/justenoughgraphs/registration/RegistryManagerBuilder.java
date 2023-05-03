package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.IRegistration;
import com.github.vfyjxf.justenoughgraphs.api.content.*;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class RegistryManagerBuilder implements IRegistration {

    private final Map<ContentType<?>, ContentInfo<?>> contentInfos;
    private final Map<ContentType<?>, TagContentInfo<?, ?>> tagContentInfos;
    private final Map<ContentType<?>, NumericalContentInfo<?>> numericalContentInfos;
    private final List<IRecipeLooker> recipeLookers;
    private final Map<RecipeType<?>, IRecipeParser<?>> recipeParsers;
    private final List<IUniversalRecipeParser> universalRecipeParsers;

    public RegistryManagerBuilder() {
        this.contentInfos = new HashMap<>();
        this.tagContentInfos = new HashMap<>();
        this.numericalContentInfos = new HashMap<>();
        this.recipeLookers = new ArrayList<>();
        this.recipeParsers = new HashMap<>();
        this.universalRecipeParsers = new ArrayList<>();
    }

    @Override
    public <T> void registerContent(
            ContentType<T> contentType,
            IContentFactory<T> factory,
            IContentHelper<T> helper,
            IContentRenderer<T> renderer
    ) {
        if (contentInfos.containsKey(contentType)) {
            throw new IllegalArgumentException("Content type " + contentType + " already registered");
        }
        contentInfos.put(contentType, new ContentInfo<>(contentType, factory, helper, renderer));
    }

    @Override
    public <STACK, VALUE> void registerTagContent(
            ContentType<STACK> stackType,
            ContentType<VALUE> valueType,
            ITagFactory<STACK, VALUE> factory,
            IContentRenderer<STACK> renderer
    ) {
        if (tagContentInfos.containsKey(valueType)) {
            throw new IllegalArgumentException("Tag Content type " + valueType + " already registered");
        }
        tagContentInfos.put(valueType, new TagContentInfo<>(valueType, stackType, factory, renderer));
    }

    @Override
    public <T> void registerNumericalContent(ContentType<T> contentType, BiFunction<T, Float, INumericalContent<T>> factory, IContentHelper<INumericalContent<T>> helper, IContentRenderer<INumericalContent<T>> renderer) {
        if (numericalContentInfos.containsKey(contentType)) {
            throw new IllegalArgumentException("Numerical Content type " + contentType + " already registered");
        }
        numericalContentInfos.put(contentType, new NumericalContentInfo<>(contentType, factory, helper, renderer));
    }

    @Override
    public void registerRecipeLooker(IRecipeLooker looker) {
        if (recipeLookers.contains(looker)) {
            throw new IllegalArgumentException("Recipe looker already registered");
        }
        recipeLookers.add(looker);
    }

    @Override
    public <T> void registerRecipeParser(RecipeType<T> recipeType, IRecipeParser<T> parser) {
        if (recipeParsers.containsKey(recipeType)) {
            throw new IllegalArgumentException("Recipe parser for " + recipeType + " already registered");
        }
        recipeParsers.put(recipeType, parser);
    }

    @Override
    public void registerUniversalRecipeParser(IUniversalRecipeParser parser) {
        if (universalRecipeParsers.contains(parser)) {
            throw new IllegalArgumentException("Universal recipe parser already registered");
        }
        universalRecipeParsers.add(parser);
    }

    public RegistryManager build() {
        return new RegistryManager(this.contentInfos, tagContentInfos, numericalContentInfos, this.recipeLookers, this.recipeParsers, this.universalRecipeParsers);
    }

}
