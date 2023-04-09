package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.*;
import com.github.vfyjxf.justenoughgraphs.api.recipe.*;
import com.github.vfyjxf.justenoughgraphs.content.CompositeContent;
import com.github.vfyjxf.justenoughgraphs.content.ItemTagContent;
import com.github.vfyjxf.justenoughgraphs.content.ListContent;
import com.github.vfyjxf.justenoughgraphs.content.TypedContent;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

@SuppressWarnings("unchecked")
public class RegistryManager implements IRegistryManager {

    private static RegistryManager instance;

    private final Map<ContentType<?>, ContentInfo<?>> registeredContents;
    private final Map<ResourceLocation, DescriptiveInfo<?, IDescriptiveContent<?>>> descriptiveInfos;
    private final List<IRecipeLooker> recipeLookers;
    private final Map<RecipeType<?>, IRecipeParser<?>> recipeParsers;
    private final List<IUniversalRecipeParser> universalRecipeParsers;

    public RegistryManager(
            Map<ContentType<?>, ContentInfo<?>> registeredContents,
            Map<ResourceLocation, DescriptiveInfo<?, IDescriptiveContent<?>>> descriptiveInfos,
            List<IRecipeLooker> recipeLookers,
            Map<RecipeType<?>, IRecipeParser<?>> recipeParsers,
            List<IUniversalRecipeParser> universalRecipeParsers
    ) {
        this.registeredContents = registeredContents;
        this.descriptiveInfos = descriptiveInfos;
        this.recipeLookers = recipeLookers;
        this.recipeParsers = recipeParsers;
        this.universalRecipeParsers = universalRecipeParsers;
        instance = this;
    }

    public static IRegistryManager getInstance() {
        return instance;
    }

    @Override
    public <T> IContent<T> createContent(T content, long amount, float chance) {
        Class<T> typeClass = (Class<T>) content.getClass();
        ContentInfo<T> info = (ContentInfo<T>) registeredContents.get(getContentType(typeClass));
        if (info == null) {
            throw new IllegalArgumentException("Unknown content type: " + typeClass);
        }
        return new TypedContent<>(info.contentType(), content, amount, chance);
    }

    @Override
    public IItemTagContent createItemTagContent(ResourceLocation tag, long amount, float chance) {
        return new ItemTagContent(tag, amount, chance);
    }

    @Override
    public ICompositeContent createCompositeContent(Collection<?> contents, long amount, float chance) {
        return new CompositeContent(contents, amount, chance);
    }

    @Override
    public <T> IListContent<T> createListContent(List<IContent<T>> contents, long amount, float chance) {
        return new ListContent<>(contents, amount, chance);
    }

    @Override
    public <T> IDescriptiveContent<T> createDescriptive(ResourceLocation identifier, T content) {
        IDescriptiveContent.IFactory<T> factory = (IDescriptiveContent.IFactory<T>) ErrorChecker.requireNonNull(
                descriptiveInfos.get(identifier),
                () -> new IllegalArgumentException("Unknown descriptive content type " + identifier)
        ).factory();

        return factory.create(content);
    }


    @Override
    public <T> ContentType<T> getContentType(Class<T> typeClass) {
        return getRegisteredContent(typeClass).contentType();
    }

    @Override
    public <T> Optional<ContentType<T>> getContentType(ResourceLocation identifier) {
        for (ContentInfo<?> info : registeredContents.values()) {
            if (info.contentType().getIdentifier().equals(identifier)) {
                return Optional.of((ContentType<T>) info.contentType());
            }
        }
        return Optional.empty();
    }

    @Override
    public <T> IContentHelper<T> getContentHelper(ContentType<T> contentType) {
        return (IContentHelper<T>) ErrorChecker.requireNonNull(
                registeredContents.get(contentType),
                () -> new IllegalArgumentException("Unknown content type: " + contentType)
        ).helper();
    }

    @Override
    public <T> IContentHelper<T> getContentHelper(Class<T> typeClass) {
        return getRegisteredContent(typeClass).helper();
    }


    @Override
    public <T> IContentRenderer<T> getContentRenderer(ContentType<T> contentType) {
        return (IContentRenderer<T>) ErrorChecker.requireNonNull(
                registeredContents.get(contentType),
                () -> new IllegalArgumentException("Unknown content type: " + contentType)
        ).renderer();
    }

    @Override
    public <T> IContentRenderer<T> getContentRenderer(Class<T> typeClass) {
        return getRegisteredContent(typeClass).renderer();
    }

    @Override
    public <T> RecipeType<T> getRecipeType(T recipe) {
        return recipeParsers.keySet().stream()
                .filter(recipeType -> recipeType.getRecipeClass().isInstance(recipe))
                .map(recipeType -> (RecipeType<T>) recipeType)
                .findFirst()
                .orElse((RecipeType<T>) RecipeTypes.UNKNOWN);
    }

    @Override
    public Collection<? extends IRecipeLooker> getRecipeLookers() {
        return new ArrayList<>(this.recipeLookers);
    }

    @Override
    public <T> IRecipeParser<T> getRecipeParser(RecipeType<T> recipeType) {
        return (IRecipeParser<T>) recipeParsers.get(recipeType);
    }

    @Override
    public List<IUniversalRecipeParser> getUniversalRecipeParsers() {
        return new ArrayList<>(this.universalRecipeParsers);
    }

    private <T> ContentInfo<T> getRegisteredContent(Class<T> typeClass) {
        for (Map.Entry<ContentType<?>, ContentInfo<?>> entry : registeredContents.entrySet()) {
            if (entry.getKey().getTypeClass().equals(typeClass)) {
                return (ContentInfo<T>) entry.getValue();
            }
        }
        throw new IllegalArgumentException("Unknown content type: " + typeClass);
    }

}
