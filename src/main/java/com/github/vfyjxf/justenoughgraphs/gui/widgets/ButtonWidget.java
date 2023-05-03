package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public class ButtonWidget extends Widget {

    private IGuiTexture buttonTexture;

    public ButtonWidget() {

    }

    public ButtonWidget(int x, int y, int width, int height, IGuiTexture buttonTexture, @Nullable BiPredicate<Screen, IInputContext> clickHandler) {
        super(x, y, width, height);
        this.buttonTexture = buttonTexture;
        this.clickHandler = clickHandler;
    }


    @Override
    public ButtonWidget setPos(int x, int y) {
        super.setPos(x, y);
        return this;
    }

    @Override
    public ButtonWidget resize(int width, int height) {
        super.resize(width, height);
        return this;
    }

    @Override
    public ButtonWidget setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        return this;
    }

    public ButtonWidget setClickHandler(BiPredicate<Screen, IInputContext> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public ButtonWidget setButtonTexture(IGuiTexture buttonTexture) {
        this.buttonTexture = buttonTexture;
        return this;
    }

    @Override
    public ButtonWidget setBackground(IGuiTexture backgroundTexture) {
        super.setBackground(backgroundTexture);
        return this;
    }

    @Override
    public ButtonWidget setHoverTexture(IGuiTexture hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    @Override
    public void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(poseStack, mouseX, mouseY, partialTick);
        if (buttonTexture != null) {
            buttonTexture.render(poseStack, x, y, width, height);
        }
    }
}
