package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

public class ContentNode<T> extends Widget implements IContentNode<T> {

    private IContent<T> content;
    private IContentRenderer<T> renderer;
    private IContentHelper<T> helper;

    public ContentNode(IContent<T> content) {
        this.content = content;
        IRegistryManager manager = IRegistryManager.getInstance();
        this.renderer = manager.getContentRenderer(content.getType());
        this.helper = manager.getContentHelper(content.getType());
        this.setSize(renderer.getWidth() + 2, renderer.getHeight() + 2);
    }

    @Override
    public ContentNode<T> setParent(@Nullable IWidgetGroup parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public ContentNode<T> setPos(int x, int y) {
        super.setPos(x, y);
        return this;
    }

    @Override
    public ContentNode<T> setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public ContentNode<T> setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        return this;
    }

    @Override
    public ContentNode<T> setBackground(IGuiTexture backgroundTexture) {
        super.setBackground(backgroundTexture);
        return this;
    }

    @Override
    public ContentNode<T> setHoverTexture(IGuiTexture hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
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
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (renderer != null) {
            poseStack.pushPose();
            {
                poseStack.translate(1, 1, 0);
                renderer.render(poseStack, content);
            }
            poseStack.popPose();
        }
    }
}
