package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Widget implements IGuiWidget {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean active = true;
    protected boolean visible = true;
    protected boolean focused = false;
    protected boolean dragging = false;
    protected IGuiTexture background;
    protected IGuiTexture hoverTexture;
    protected BiPredicate<Screen, IInputContext> clickHandler;
    protected BiPredicate<Screen, IInputContext> keyHandler;
    protected BiConsumer<PoseStack, Widget> extraRender;

    @Nullable
    protected IWidgetGroup parent;
    protected List<Component> tooltip;

    public Widget(
            int x, int y,
            int width, int height,
            @Nullable IWidgetGroup parent,
            @Nullable IGuiTexture background,
            @Nullable IGuiTexture hoverTexture,
            @Nullable BiPredicate<Screen, IInputContext> clickHandler,
            @Nullable BiPredicate<Screen, IInputContext> keyHandler
    ) {
        this(x, y, width, height);
        this.parent = parent;
        this.background = background;
        this.hoverTexture = hoverTexture;
        this.clickHandler = clickHandler;
        this.keyHandler = keyHandler;
    }

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Widget(@Nullable IWidgetGroup parent) {
        this(0, 0, 0, 0, parent, null, null, null, null);
    }

    public Widget() {
    }

    public Widget setExtraRender(BiConsumer<PoseStack, Widget> extraRender) {
        this.extraRender = extraRender;
        return this;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<Component> getTooltips() {
        return tooltip;
    }

    @Override
    public IGuiWidget setPos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IGuiWidget resize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public IGuiWidget setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (parent != null) {
            mouseX -= parent.getX();
            mouseY -= parent.getY();
        }
        return BoundChecker.checkOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public @Nullable IWidgetGroup getParent() {
        return parent;
    }

    @Override
    public IGuiWidget setParent(@Nullable IWidgetGroup parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public IGuiWidget setBackground(IGuiTexture backgroundTexture) {
        this.background = backgroundTexture;
        return this;
    }

    @Override
    public IGuiWidget setHoverTexture(IGuiTexture hoverTexture) {
        this.hoverTexture = hoverTexture;
        return this;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        {
            poseStack.translate(this.getX(), this.getY(), 0);
            this.renderBackground(poseStack, mouseX, mouseY, partialTick);
            this.renderForeground(poseStack, mouseX, mouseY, partialTick);
            if (extraRender != null) extraRender.accept(poseStack, this);
        }
        poseStack.popPose();
    }

    public void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (background != null) {
            background.render(poseStack, this.getX(), this.getY());
        }
        if (hoverTexture != null && isMouseOver(mouseX, mouseY)) {
            hoverTexture.render(poseStack, this.getX(), this.getY());
        }
    }

    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public boolean handleMouseClick(@Nullable Screen screen, IInputContext context) {
        return clickHandler != null && clickHandler.test(screen, context);
    }

    @Override
    public boolean handleKeyTyped(@Nullable Screen screen, IInputContext context) {
        return keyHandler != null && keyHandler.test(screen, context);
    }
}
