package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class ColorRectTexture implements IGuiTexture {
    private int color;
    private final int width;
    private final int height;

    public ColorRectTexture(int color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public ColorRectTexture(Color color, int width, int height) {
        this.color = color.getRGB();
        this.width = width;
        this.height = height;
    }

    public ColorRectTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        Gui.fill(stack, xOffset, yOffset, xOffset + width, yOffset + height, color);
        RenderSystem.enableBlend();
    }
}
