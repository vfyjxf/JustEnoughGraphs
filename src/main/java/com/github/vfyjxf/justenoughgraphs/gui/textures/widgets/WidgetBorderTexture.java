package com.github.vfyjxf.justenoughgraphs.gui.textures.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IWidgetTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;

public class WidgetBorderTexture implements IWidgetTexture {

    private final IGuiWidget widget;
    private int border;
    private int color;

    private WidgetBorderTexture(IGuiWidget widget) {
        this.widget = widget;
        this.border = 1;
        this.color = 0xFFFFFFFF;
    }

    public int getBorder() {
        return border;
    }

    public WidgetBorderTexture setBorder(int border) {
        this.border = border;
        return this;
    }

    public int getColor() {
        return color;
    }

    public WidgetBorderTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public int getWidth() {
        return widget.getWidth();
    }

    @Override
    public int getHeight() {
        return widget.getHeight();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        int width = getWidth();
        int height = getHeight();
        GuiComponent.fill(stack, xOffset, yOffset, xOffset + width, yOffset + border, color);
        GuiComponent.fill(stack, xOffset, yOffset + height - border, xOffset + width, yOffset + height, color);
        GuiComponent.fill(stack, xOffset, yOffset + border, xOffset + border, yOffset + height - border, color);
        GuiComponent.fill(stack, xOffset + width - border, yOffset + border, xOffset + width, yOffset + height - border, color);
    }

    @Override
    public @NotNull IGuiWidget getWidget() {
        return widget;
    }
}
