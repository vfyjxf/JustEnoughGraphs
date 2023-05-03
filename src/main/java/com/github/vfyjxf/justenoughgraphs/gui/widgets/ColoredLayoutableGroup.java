package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.ILayoutableGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.gui.textures.ColorBorderTexture;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.layoutable.LayoutableWidgetGroup;

import java.awt.*;

public class ColoredLayoutableGroup extends LayoutableWidgetGroup implements IColoredWidget {

    protected ColorBorderTexture borderTexture;

    public ColoredLayoutableGroup() {
        this.setBackground(borderTexture = new ColorBorderTexture(1, Color.WHITE, Color.WHITE, getWidth(), getHeight()));

    }

    @Override
    public int getBackgroundColor() {
        return borderTexture.getColor();
    }

    @Override
    public int getBorderColor() {
        return borderTexture.getBorderColor();
    }

    @Override
    public ColoredLayoutableGroup setBackground(IGuiTexture backgroundTexture) {
        super.setBackground(backgroundTexture);
        return this;
    }

    @Override
    public ColoredLayoutableGroup setHoverTexture(IGuiTexture hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    @Override
    public ILayoutableGroup setPos(int x, int y) {
        return super.setPos(x, y);
    }

    @Override
    public ColoredLayoutableGroup setBackgroundColor(int color) {
        borderTexture.setColor(color);
        return this;
    }

    @Override
    public ColoredLayoutableGroup setBorderColor(int color) {
        borderTexture.setBorderColor(color);
        return this;
    }

    @Override
    public ColoredLayoutableGroup setBorderWidth(int width) {
        borderTexture.setBorder(width);
        return this;
    }

}
