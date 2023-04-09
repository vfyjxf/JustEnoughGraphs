package com.github.vfyjxf.justenoughgraphs.integration.jegh.recipe.parser;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IItemTagContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IUniversalRecipeParser;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IngredientType;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.integration.jei.JEGhPlugin;
import com.github.vfyjxf.justenoughgraphs.mixin.RecipeLayoutBuilderAccessor;
import com.github.vfyjxf.justenoughgraphs.mixin.RecipeManagerAccessor;
import com.github.vfyjxf.justenoughgraphs.mixin.RecipeManagerInternalAccessor;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IRecipeCatalystLookup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import mezz.jei.library.ingredients.IIngredientSupplier;
import mezz.jei.library.util.IngredientSupplierHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Use JEI api to parse recipes.
 * It does not support recipes that are not registered in jei, and it does not support chance recipes.
 * Warning: This parser should not be used by anyone other than Jegh,
 * it relies heavily on the specific implementation details of jei.
 *
 * @author vfyjxf
 */
public class JeiRecipeParser implements IUniversalRecipeParser {

    @Override
    public <T> boolean isSupport(RecipeType<T> recipeType) {
        return true;
    }

    @Override
    public Map<IngredientType, List<IContent<?>>> parse(Object recipe) {
        return parseHelper(recipe);
    }

    @Override
    public Collection<IContent<?>> parseByType(Object recipe, IngredientType type) {
        return parseHelper(recipe).getOrDefault(type, Collections.emptyList());
    }

    private static <T> Map<IngredientType, List<IContent<?>>> parseHelper(T recipe) {
        Pair<mezz.jei.api.recipe.RecipeType<T>, IIngredientSupplier> pair = getIngredientSupplier(recipe);
        if (pair.getRight() == null)
            return Collections.emptyMap();
        Stream<IRecipeLayoutSlotSource> slotStream = ((RecipeLayoutBuilderAccessor) pair.getRight()).callSlotStream();
        Map<IngredientType, List<IContent<?>>> ingredients = slotStream.collect(Collectors.groupingBy(
                slotSource -> mapTo(slotSource.getRole()),
                () -> new EnumMap<>(IngredientType.class),
                Collectors.mapping(JeiRecipeParser::covertToContent, Collectors.toList())));
        if (pair.getLeft() != null) {
            IContent<?> catalyst = parseCatalysts(pair.getLeft());
            if (catalyst != null) {
                ingredients.computeIfAbsent(IngredientType.CATALYST, k -> new ArrayList<>()).add(catalyst);
            }
        }
        for (List<IContent<?>> list : ingredients.values()) {
            list.removeIf(Objects::isNull);
        }
        return ingredients;
    }

    @Nullable
    private static <T> IContent<?> covertToContent(IRecipeLayoutSlotSource slotSource) {
        IRegistryManager registryManager = IRegistryManager.getInstance();
        List<IIngredientType<?>> allTypes = slotSource.getIngredientTypes().toList();
        boolean singleType = allTypes.size() == 1;
        if (singleType) {
            IIngredientType<T> firstType = (IIngredientType<T>) allTypes.get(0);
            //we know all ingredients are of the same type
            List<T> allIngredients = slotSource.getIngredients(firstType).toList();
            return convertSingleType(registryManager, firstType, allIngredients);
        } else {

        }
        return null;
    }

    @Nullable
    private static <T> IContent<?> parseCatalysts(mezz.jei.api.recipe.RecipeType<T> recipeType) {
        IRecipeCatalystLookup lookup = JEGhPlugin.getRecipeManager().createRecipeCatalystLookup(recipeType);
        List<? extends IIngredientType<?>> allTypes = lookup.get().map(ITypedIngredient::getType).toList();
        boolean singleType = allTypes.size() == 1;
        if (singleType) {
            IIngredientType<T> firstType = (IIngredientType<T>) allTypes.get(0);
            List<T> allIngredients = lookup.get(firstType).toList();
            return convertSingleType(IRegistryManager.getInstance(), firstType, allIngredients);
        } else {

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> IContent<?> convertSingleType(IRegistryManager registryManager, IIngredientType<T> type, List<T> allIngredients) {
        if (allIngredients.isEmpty()) return null;
        IContentHelper<T> contentHelper = registryManager.getContentHelper((Class<T>) type.getIngredientClass());
        long amount;
        if (allIngredients.size() == 1) {
            T firstIngredient = allIngredients.get(0);
            amount = contentHelper.getAmount(firstIngredient);
            return registryManager.createContent(firstIngredient, amount);
        } else {
            amount = allIngredients.stream().findFirst().map(contentHelper::getAmount).orElse(-1L);
            Optional<ResourceLocation> maybeTag = contentHelper.getTags(allIngredients).stream().findFirst();
            ContentType<T> contentType = (ContentType<T>) registryManager.getContentType(type.getIngredientClass());
            IItemTagContent tagContent = maybeTag.map(tag ->
                    registryManager.createItemTagContent(tag, amount, 1.0f)
            ).orElse(null);
            if (tagContent != null) {
                return tagContent;
            }
        }
        List<IContent<T>> contents = allIngredients.stream()
                .map(ingredient -> registryManager.createContent(ingredient, amount))
                .toList();
        return registryManager.createListContent(contents, amount, 1.0f);
    }

    private static IngredientType mapTo(RecipeIngredientRole role) {
        return switch (role) {
            case INPUT -> IngredientType.INPUT;
            case OUTPUT -> IngredientType.OUTPUT;
            case CATALYST -> IngredientType.CATALYST;
            case RENDER_ONLY -> IngredientType.OTHER;
        };
    }

    private static <T> Pair<mezz.jei.api.recipe.RecipeType<T>, IIngredientSupplier> getIngredientSupplier(T recipe) {
        IRecipeManager recipeManager = JEGhPlugin.getRecipeManager();
        List<IRecipeCategory<?>> recipeCategories = ((RecipeManagerInternalAccessor) ((RecipeManagerAccessor) recipeManager).getInternal())
                .getRecipeCategories();
        for (IRecipeCategory<?> current : recipeCategories) {
            if (current.getRecipeType().getRecipeClass().isInstance(recipe)) {
                IRecipeCategory<T> recipeCategory = (IRecipeCategory<T>) current;
                return Pair.of(recipeCategory.getRecipeType(), IngredientSupplierHelper.getIngredientSupplier(recipe, recipeCategory, JEGhPlugin.getIngredientManager()));
            }
        }
        return ImmutablePair.nullPair();
    }

}
