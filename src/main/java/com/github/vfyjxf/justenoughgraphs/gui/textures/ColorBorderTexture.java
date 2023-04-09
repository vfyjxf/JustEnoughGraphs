package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class ColorBorderTexture implements IGuiTexture {

    private int color;
    private int border;
    private final int width;
    private final int height;

    public ColorBorderTexture(int border, int color, int width, int height) {
        this.color = color;
        this.border = border;
        this.width = width;
        this.height = height;
    }

    public ColorBorderTexture(int border, Color color, int width, int height) {
        this.color = color.getRGB();
        this.border = border;
        this.width = width;
        this.height = height;
    }

    public ColorBorderTexture setBorder(int border) {
        this.border = border;
        return this;
    }

    public ColorBorderTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getColor() {
        return color;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        drawSolidRect(stack, xOffset - border, yOffset - border, width + 2 * border, border, color);
        drawSolidRect(stack, xOffset - border, yOffset + height, width + 2 * border, border, color);
        drawSolidRect(stack, xOffset - border, yOffset, border, height, color);
        drawSolidRect(stack, xOffset + width, yOffset, border, height, color);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawSolidRect(PoseStack stack, int xOffset, int yOffset, int width, int height, int color) {
        Gui.fill(stack, xOffset, yOffset, xOffset + width, yOffset + height, color);
        RenderSystem.enableBlend();
    }

}
