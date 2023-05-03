package com.github.vfyjxf.justenoughgraphs.gui.widgets.tree;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.gui.screen.recipe.tree.RecipeTreeScreen;
import com.github.vfyjxf.justenoughgraphs.gui.textures.ColorBorderTexture;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.IColoredWidget;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * A node with a single colour border and background.
 */
non-sealed class ColoredTreeNode<T> extends AbstractRecipeTreeNode<T> implements IColoredWidget {


    protected ColorBorderTexture borderTexture;

    public ColoredTreeNode(
            IContent<T> content,
            RecipeTreeScreen.TreeContext context,
            Map<RecipeType<?>, List<?>> recipeMap,
            @Nullable Object recipe
    ) {
        super(content, context, recipeMap, recipe);
        this.setBackground(borderTexture = new ColorBorderTexture(1, Color.WHITE, Color.WHITE, getWidth(), getHeight()));
    }


    @Override
    public int getBackgroundColor() {
        return ((ColorBorderTexture) background).getColor();
    }

    @Override
    public int getBorderColor() {
        return borderTexture.getBorderColor();
    }

    @Override
    public ColoredTreeNode<T> setBackgroundColor(int color) {
        borderTexture.setColor(color);
        return this;
    }

    @Override
    public ColoredTreeNode<T> setBorderColor(int color) {
        borderTexture.setBorderColor(color);
        return this;
    }

    @Override
    public ColoredTreeNode<T> setBorderWidth(int width) {
        borderTexture.setBorder(width);
        return this;
    }

}
