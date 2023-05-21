package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiTextureGroup implements IGuiTexture {

    private final IGuiTexture[] textures;

    public GuiTextureGroup(IGuiTexture... textures) {
        this.textures = textures;
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
        for (IGuiTexture texture : textures) {
            texture.render(stack, mouseX, mouseY, xOffset, yOffset);
        }
    }
}
