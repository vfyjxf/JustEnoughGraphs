package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.gui.textures.ColorBorderTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * ContentNode without special features
 */
public class ColoredContentNode<T> extends Widget implements IContentNode<T>, IColoredWidget {

    public static <T> ColoredContentNode<T> create(IContent<T> content) {
        return new ColoredContentNode<>(content, 0);
    }

    public static <T> ColoredContentNode<T> createWithBorder(IContent<T> content, int borderColor) {
        return createWithBorder(content, 1, borderColor);
    }

    public static <T> ColoredContentNode<T> createWithBorder(IContent<T> content, int border, int borderColor) {
        return new ColoredContentNode<>(content, border)
                .setBackgroundColor(0x000000)
                .setBorderColor(borderColor);
    }

    public static <T> ColoredContentNode<T> copy(IContentNode<T> node) {
        return new ColoredContentNode<>(node.getContent(), 0);
    }

    private IContent<T> content;
    private IContentRenderer<T> renderer;
    private IContentHelper<T> helper;
    private final ColorBorderTexture borderTexture;
    private int border;

    public ColoredContentNode(IContent<T> content, int border) {
        this.content = content;
        IRegistryManager manager = IRegistryManager.getInstance();
        this.renderer = manager.getContentRenderer(content.getType());
        this.helper = manager.getContentHelper(content.getType());
        this.border = border;
        this.setSize(renderer.getWidth() + border * 2, renderer.getHeight() + border * 2);
        this.setBackground(this.borderTexture = new ColorBorderTexture(1, Color.WHITE, Color.WHITE, getWidth(), getHeight()));
        this.borderTexture.setBorder(border);
    }


    @Override
    public IContent<T> getContent() {
        return content;
    }

    @Override
    public <V> IContentNode<V> setContent(IContent<V> content) {
        this.content = (IContent<T>) content;
        IRegistryManager manager = IRegistryManager.getInstance();
        this.renderer = manager.getContentRenderer(this.content.getType());
        this.helper = manager.getContentHelper(this.content.getType());
        this.setSize(renderer.getWidth() + 2, renderer.getHeight() + 2);
        return (IContentNode<V>) this;
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
    public ColoredContentNode<T> setBackgroundColor(int color) {
        borderTexture.setColor(color);
        return this;
    }

    @Override
    public ColoredContentNode<T> setBorderColor(int color) {
        borderTexture.setBorderColor(color);
        return this;
    }

    @Override
    public ColoredContentNode<T> setBorderWidth(int width) {
        this.border = width;
        borderTexture.setBorder(width);
        return this;
    }

    @Override
    public ColoredContentNode<T> setPos(int x, int y) {
        super.setPos(x, y);
        return this;
    }

    @Override
    public ColoredContentNode<T> setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public ColoredContentNode<T> setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        return this;
    }

    @Override
    public ColoredContentNode<T> setParent(@Nullable IWidgetGroup parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public ColoredContentNode<T> setBackground(IGuiTexture backgroundTexture) {
        super.setBackground(backgroundTexture);
        return this;
    }

    @Override
    public ColoredContentNode<T> setHoverTexture(IGuiTexture hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (renderer != null) {
            poseStack.pushPose();
            {
                poseStack.translate(border, border, 0);
                renderer.render(poseStack, content);
            }
            poseStack.popPose();
        }
    }
}
