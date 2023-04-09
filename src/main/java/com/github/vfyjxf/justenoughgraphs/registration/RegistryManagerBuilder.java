package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.IRegistration;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.IDescriptiveContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryManagerBuilder implements IRegistration {

    private final Map<ContentType<?>, ContentInfo<?>> contentInfos;
    private final Map<ResourceLocation, DescriptiveInfo<?, IDescriptiveContent<?>>> descriptiveInfos;
    private final List<IRecipeLooker> recipeLookers;
    private final Map<RecipeType<?>, IRecipeParser<?>> recipeParsers;
    private final List<IUniversalRecipeParser> universalRecipeParsers;

    public RegistryManagerBuilder() {
        this.contentInfos = new HashMap<>();
        this.descriptiveInfos = new HashMap<>();
        this.recipeLookers = new ArrayList<>();
        this.recipeParsers = new HashMap<>();
        this.universalRecipeParsers = new ArrayList<>();
    }

    @Override
    public <T> void registerContent(ContentType<T> contentType, IContentHelper<T> helper, IContentRenderer<T> renderer) {
        if (contentInfos.containsKey(contentType)) {
            throw new IllegalArgumentException("Content type " + contentType + " already registered");
        }
        contentInfos.put(contentType, new ContentInfo<>(contentType, helper, renderer));
    }

    @Override
    public <V, T extends IDescriptiveContent<V>> void registerDescriptive(ResourceLocation identifier, IContentHelper<T> helper, IContentRenderer<T> renderer, IDescriptiveContent.IFactory<T> factory) {
        if (descriptiveInfos.containsKey(identifier)) {
            throw new IllegalArgumentException("Descriptive content " + identifier + " already registered");
        }
        descriptiveInfos.put(identifier, (DescriptiveInfo<?, IDescriptiveContent<?>>) new DescriptiveInfo<>(identifier, helper, renderer, factory));
    }

    @Override
    public void registerRecipeLooker(IRecipeLooker looker) {
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
        universalRecipeParsers.add(parser);
    }

    public RegistryManager build() {
        return new RegistryManager(this.contentInfos, this.descriptiveInfos, this.recipeLookers, this.recipeParsers, this.universalRecipeParsers);
    }

}
