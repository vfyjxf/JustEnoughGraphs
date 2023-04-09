package com.github.vfyjxf.justenoughgraphs.integration.jei;

import com.github.vfyjxf.justenoughgraphs.api.Globals;
import com.google.common.base.Preconditions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEGhPlugin implements IModPlugin {

    private static IRecipeManager recipeManager;
    private static IIngredientManager ingredientManager;
    private static IFocusFactory focusFactory;

    public static IIngredientManager getIngredientManager() {
        Preconditions.checkState(ingredientManager != null, "IngredientManager is not initialized yet.");
        return ingredientManager;
    }

    public static IRecipeManager getRecipeManager() {
        Preconditions.checkState(recipeManager != null, "RecipeManager is not initialized yet.");
        return recipeManager;
    }

    public static IFocusFactory getFocusFactory() {
        Preconditions.checkState(focusFactory != null, "FocusFactory is not initialized yet.");
        return focusFactory;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return Globals.JEI_PLUGIN_UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
    }

    @Override
    public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
        IModPlugin.super.registerFluidSubtypes(registration, platformFluidHelper);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IModPlugin.super.registerIngredients(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        IModPlugin.super.registerAdvanced(registration);
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        IModPlugin.super.registerRuntime(registration);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        ingredientManager = jeiRuntime.getIngredientManager();
        recipeManager = jeiRuntime.getRecipeManager();
        focusFactory = jeiRuntime.getJeiHelpers().getFocusFactory();
    }

    @Override
    public void onRuntimeUnavailable() {
        IModPlugin.super.onRuntimeUnavailable();
    }
}
