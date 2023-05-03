package com.github.vfyjxf.justenoughgraphs.api;

import com.github.vfyjxf.justenoughgraphs.api.content.*;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.registration.RegistryManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A manager for all the registries.
 */
public interface IRegistryManager {

    static IRegistryManager getInstance() {
        IRegistryManager manager = RegistryManager.getInstance();
        if (manager == null) {
            throw new IllegalStateException("RegistryManager is not initialized yet.");
        }
        return manager;
    }

    <T> IContent<T> createContent(T content, long amount, float chance);

    default <T> IContent<T> createContent(T content, long amount) {
        return createContent(content, amount, 1.0f);
    }

    @Nullable <STACK, VALUE> ITagContent<STACK, VALUE> createTagContent(ContentType<STACK> stackType, ResourceLocation tag, long amount, float chance);

    @Nullable
    default ITagContent<ItemStack, Item> createItemTagContent(ResourceLocation tag, long amount, float chance) {
        return createTagContent(ContentTypes.ITEM_STACK, tag, amount, chance);
    }

    <T> INumericalContent<T> createNumericalContent(T content, float chance);

    <T> IListContent<T> createListContent(List<IContent<T>> contents, long amount, float chance);

    ICompositeContent createCompositeContent(Collection<?> contents, long amount, float chance);

    <T> ContentType<T> getContentType(Class<T> typeClass);

    <T> Optional<ContentType<T>> getContentType(ResourceLocation identifier);

    <T> Optional<ContentType<T>> getTagType(ResourceLocation identifier);

    /**
     * Get the content type of the given content.
     * It not return anything about descriptive content.
     */
    default <T> ContentType<T> getContentType(T content) {
        return getContentType((Class<T>) content.getClass());
    }

    <T> IContentHelper<T> getContentHelper(ContentType<T> contentType);

    default <T> IContentHelper<T> getContentHelper(T content) {
        return getContentHelper((Class<T>) content.getClass());
    }

    <T> IContentHelper<T> getContentHelper(Class<T> typeClass);

    <T> IContentRenderer<T> getContentRenderer(ContentType<T> typeClass);

    default <T> IContentRenderer<T> getContentRenderer(T content) {
        return getContentRenderer((Class<T>) content.getClass());
    }

    <T> IContentRenderer<T> getContentRenderer(Class<T> typeClass);

    <T> IContentRenderer<ITagContent<T, ?>> getTagRenderer(ContentType<T> stackType);

    <T> RecipeType<T> getRecipeType(T recipe);

    Collection<? extends IRecipeLooker> getRecipeLookers();

    <T> IRecipeParser<T> getRecipeParser(RecipeType<T> recipeType);

    default <T> Optional<IRecipeParser<T>> getRecipeParser(T recipe) {
        return Optional.ofNullable(getRecipeParser(getRecipeType(recipe)));
    }

    List<IUniversalRecipeParser> getUniversalRecipeParsers();

}
