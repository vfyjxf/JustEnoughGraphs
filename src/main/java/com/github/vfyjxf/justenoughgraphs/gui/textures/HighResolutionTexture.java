package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;

public class HighResolutionTexture implements IGuiTexture {

    private final IGuiTexture texture;
    private final int scale;

    public HighResolutionTexture(IGuiTexture texture, int scale) {
        int width = texture.getWidth();
        int height = texture.getHeight();
        if (width % scale != 0) {
            throw new IllegalArgumentException(String.format("drawable width %s must be divisible by the scale %s", width, scale));
        }
        if (height % scale != 0) {
            throw new IllegalArgumentException(String.format("drawable height %s must be divisible by the scale %s", height, scale));
        }

        this.texture = texture;
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return texture.getWidth() / scale;
    }

    @Override
    public int getHeight() {
        return texture.getHeight() / scale;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, int xOffset, int yOffset) {
        stack.pushPose();
        {
            stack.translate(xOffset, yOffset, 0);
            stack.scale(1 / (float) scale, 1 / (float) scale, 1);
            this.texture.render(stack, mouseX, mouseY);
        }
        stack.popPose();
    }


}
