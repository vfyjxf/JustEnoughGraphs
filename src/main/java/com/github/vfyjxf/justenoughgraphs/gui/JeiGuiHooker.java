package com.github.vfyjxf.justenoughgraphs.gui;

import com.github.vfyjxf.justenoughgraphs.gui.screen.recipe.tree.RecipeTreeScreen;
import com.github.vfyjxf.justenoughgraphs.mixin.jei.RecipesGuiAccessor;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class JeiGuiHooker {

    private RecipesGui recipesGui;
    private List<Button> openTreeButtons = new ArrayList<>();

    @SubscribeEvent
    public void onGuiInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof RecipesGui) {
            recipesGui = (RecipesGui) event.getScreen();
            for (Button openTreeButton : openTreeButtons) {
                event.getScreen().renderables.remove(openTreeButton);
            }
            List<IRecipeLayoutDrawable<?>> recipeLayouts = ((RecipesGuiAccessor) recipesGui).getRecipeLayouts();
            openTreeButtons.clear();
            for (IRecipeLayoutDrawable<?> recipeLayout : recipeLayouts) {
                Rect2i buttonArea = recipeLayout.getRecipeTransferButtonArea();
                Button openTreeButton = new Button(
                        buttonArea.getX() + 16,
                        buttonArea.getY() + 8,
                        buttonArea.getWidth(),
                        buttonArea.getHeight(),
                        Component.empty(),
                        button -> Minecraft.getInstance().setScreen(RecipeTreeScreen.openTest(recipeLayout.getRecipe())));
                event.getScreen().renderables.add(openTreeButton);
                openTreeButtons.add(openTreeButton);
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof RecipesGui) {
            for (Button openTreeButton : openTreeButtons) {
                openTreeButton.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
            }
        }
    }

    @SubscribeEvent
    public void onMouseHandled(ScreenEvent.MouseButtonPressed.Post event) {
        if (event.getScreen() instanceof RecipesGui) {
            for (Button openTreeButton : openTreeButtons) {
                openTreeButton.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
            }
        }
    }


}
