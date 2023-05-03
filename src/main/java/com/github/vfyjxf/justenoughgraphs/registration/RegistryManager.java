package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.*;
import com.github.vfyjxf.justenoughgraphs.api.recipe.*;
import com.github.vfyjxf.justenoughgraphs.content.CompositeContent;
import com.github.vfyjxf.justenoughgraphs.content.ListContent;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unchecked")
public class RegistryManager implements IRegistryManager {

    private static RegistryManager instance;

    private final Map<ContentType<?>, ContentInfo<?>> registeredContents;
    private final Map<ContentType<?>, TagContentInfo<?, ?>> tagContentInfos;
    private final Map<ContentType<?>, NumericalContentInfo<?>> numericalContentInfos;
    private final List<IRecipeLooker> recipeLookers;
    private final Map<RecipeType<?>, IRecipeParser<?>> recipeParsers;
    private final List<IUniversalRecipeParser> universalRecipeParsers;

    public RegistryManager(
            Map<ContentType<?>, ContentInfo<?>> registeredContents,
            Map<ContentType<?>, TagContentInfo<?, ?>> tagContentInfos,
            Map<ContentType<?>, NumericalContentInfo<?>> numericalContentInfos,
            List<IRecipeLooker> recipeLookers,
            Map<RecipeType<?>, IRecipeParser<?>> recipeParsers,
            List<IUniversalRecipeParser> universalRecipeParsers
    ) {
        this.registeredContents = ImmutableMap.copyOf(registeredContents);
        this.tagContentInfos = ImmutableMap.copyOf(tagContentInfos);
        this.numericalContentInfos = ImmutableMap.copyOf(numericalContentInfos);
        this.recipeLookers = ImmutableList.copyOf(recipeLookers);
        this.recipeParsers = ImmutableMap.copyOf(recipeParsers);
        this.universalRecipeParsers = ImmutableList.copyOf(universalRecipeParsers);
        instance = this;
    }

    @Nullable
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
        return info.factory().create(content, amount, chance);
    }

    @Override
    public <STACK, VALUE> ITagContent<STACK, VALUE> createTagContent(ContentType<STACK> stackType, ResourceLocation tag, long amount, float chance) {
        TagContentInfo<STACK, VALUE> info = getTagInfo(stackType);
        if (info != null) {
            return info.factory().create(tag, amount, chance);
        }
        return null;
    }

    @Override
    public <T> INumericalContent<T> createNumericalContent(T content, float chance) {
        Class<T> typeClass = (Class<T>) content.getClass();
        NumericalContentInfo<T> info = (NumericalContentInfo<T>) numericalContentInfos.get(getContentType(typeClass));
        if (info == null) {
            throw new IllegalArgumentException("Unknown content type: " + typeClass);
        }
        return info.factory().apply(content, chance);
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
    public <T> Optional<ContentType<T>> getTagType(ResourceLocation identifier) {
        for (TagContentInfo<?, ?> value : tagContentInfos.values()) {
            if (value.valueType().getIdentifier().equals(identifier)) {
                return Optional.of((ContentType<T>) value.valueType());
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
    public <T> IContentRenderer<ITagContent<T, ?>> getTagRenderer(ContentType<T> stackType) {
        TagContentInfo<T, ?> info = getTagInfo(stackType);
        if (info == null) {
            throw new IllegalArgumentException("Unknown tag content type: " + stackType);
        }

        return (IContentRenderer) info.renderer();
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

    @Nullable
    private <STACK, VALUE> TagContentInfo<STACK, VALUE> getTagInfo(ContentType<STACK> stackType) {
        for (Map.Entry<ContentType<?>, TagContentInfo<?, ?>> entry : tagContentInfos.entrySet()) {
            if (entry.getValue().stackType().equals(stackType))
                return (TagContentInfo<STACK, VALUE>) entry.getValue();
        }
        return null;
    }

}
