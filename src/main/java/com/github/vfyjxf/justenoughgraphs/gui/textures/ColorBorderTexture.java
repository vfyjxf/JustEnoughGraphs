package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class ColorBorderTexture implements IGuiTexture {

    private int color;
    private int borderColor;
    private int border;
    private final int width;
    private final int height;

    public ColorBorderTexture(int border, int borderColor, int color, int width, int height) {
        this.color = color;
        this.borderColor = borderColor;
        this.border = border;
        this.width = width;
        this.height = height;
    }

    public ColorBorderTexture(int border, Color borderColor, Color color, int width, int height) {
        this.color = color.getRGB();
        this.borderColor = borderColor.getRGB();
        this.border = border;
        this.width = width;
        this.height = height;
    }

    @CanIgnoreReturnValue
    public ColorBorderTexture setBorder(int border) {
        this.border = border;
        return this;
    }

    @CanIgnoreReturnValue
    public ColorBorderTexture setColor(int color) {
        this.color = color;
        return this;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public int getBorder() {
        return this.border;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getColor() {
        return color;
    }

    public int getBorderColor() {
        return borderColor;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        //render border
        GuiComponent.fill(stack, xOffset, yOffset, xOffset + width, yOffset + border, borderColor);
        GuiComponent.fill(stack, xOffset, yOffset + height - border, xOffset + width, yOffset + height, borderColor);
        GuiComponent.fill(stack, xOffset, yOffset + border, xOffset + border, yOffset + height - border, borderColor);
        GuiComponent.fill(stack, xOffset + width - border, yOffset + border, xOffset + width, yOffset + height - border, borderColor);

        //render background
        GuiComponent.fill(stack, xOffset + border, yOffset + border, xOffset + width - border, yOffset + height - border, color);
    }

    @OnlyIn(Dist.CLIENT)
    public static void draw(PoseStack stack, int xOffset, int yOffset, int width, int height, int color) {
        Gui.fill(stack, xOffset, yOffset, xOffset + width, yOffset + height, color);
        RenderSystem.enableBlend();
    }

}
