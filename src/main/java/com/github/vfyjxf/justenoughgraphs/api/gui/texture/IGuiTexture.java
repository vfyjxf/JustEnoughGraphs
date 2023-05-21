package com.github.vfyjxf.justenoughgraphs.api.gui.texture;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IGuiTexture {

    int getWidth();

    int getHeight();

    void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset);

    default void render(PoseStack stack, int mouseX, int mouseY) {
        render(stack, mouseX, mouseY, 0, 0);
    }

    default void renderSubArea(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        render(stack, mouseX, mouseY, xOffset, yOffset);
    }

}
