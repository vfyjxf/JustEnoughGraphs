package com.github.vfyjxf.justenoughgraphs.integration.jegh.recipe.looker;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.helper.RecipeHelper;
import com.github.vfyjxf.justenoughgraphs.integration.jei.JEGhPlugin;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.gui.ingredients.IngredientLookupState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public class JeiRecipeLooker implements IRecipeLooker {

    @Override
    public <T> boolean isSupport(RecipeType<T> recipeType) {
        IRecipeManager recipeManager = JEGhPlugin.getRecipeManager();
        ResourceLocation typeId = recipeType.getId();
        return recipeManager.getRecipeType(typeId).isPresent();
    }

    @Override
    public <T> Map<RecipeType<?>, Collection<?>> lookForRecipes(IContent<T> result) {
        return lookHelper(result.getContent(), Collections.singletonList(RecipeIngredientRole.OUTPUT));
    }

    @Override
    public <T> Map<RecipeType<?>, Collection<?>> lookForRecipesByInput(IContent<T> input) {
        return lookHelper(input.getContent(), List.of(RecipeIngredientRole.INPUT, RecipeIngredientRole.CATALYST));
    }

    private static <T> Map<RecipeType<?>, Collection<?>> lookHelper(T focusValue, List<RecipeIngredientRole> roles) {
        IFocusFactory focusFactory = JEGhPlugin.getFocusFactory();
        IRecipeManager recipeManager = JEGhPlugin.getRecipeManager();
        IIngredientManager ingredientManager = JEGhPlugin.getIngredientManager();
        IFocusGroup focuses = ingredientManager.getIngredientTypeChecked(focusValue)
                .map(type -> roles.stream()
                        .map(role -> focusFactory.createFocus(role, type, focusValue))
                        .collect(Collectors.toList())
                )
                .map(focusFactory::createFocusGroup)
                .orElse(null);
        if (focuses == null)
            return Collections.emptyMap();

        IngredientLookupState state = IngredientLookupState.createWithFocus(recipeManager, focuses);
        Map<RecipeType<?>, Collection<?>> recipeMap = new HashMap<>(state.getRecipeCategories().size() + 1);
        @Unmodifiable List<IRecipeCategory<?>> recipeCategories = state.getRecipeCategories();
        for (int index = 0; index < recipeCategories.size(); index++) {
            IRecipeCategory<?> recipeCategory = recipeCategories.get(index);
            lookByType(recipeMap, state, recipeCategory, index);
        }
        return recipeMap;
    }

    private static <T> void lookByType(Map<RecipeType<?>, Collection<?>> recipeMap, IngredientLookupState state, IRecipeCategory<T> recipeCategory, int index) {
        RecipeType<T> recipeType = RecipeHelper.toJeghType(recipeCategory.getRecipeType());
        state.setRecipeIndex(index);
        Collection<T> focusedRecipes = (Collection<T>) state.getFocusedRecipes().getRecipes();
        recipeMap.put(recipeType, focusedRecipes);
    }

}
