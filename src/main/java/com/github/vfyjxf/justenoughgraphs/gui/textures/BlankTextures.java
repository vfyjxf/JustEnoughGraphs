package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;

public class BlankTextures implements IGuiTexture {

    public static final BlankTextures BLANK_TEXTURES = new BlankTextures();

    @Override
    public IGuiTexture setColor(int color) {
        return this;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        //NOOP
    }

    @Override
    public void renderSubArea(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        //NOOP
    }
}
