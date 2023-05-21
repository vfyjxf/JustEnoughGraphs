package com.github.vfyjxf.justenoughgraphs.api.gui;

import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface IWidgetGroup extends IGuiWidget {

    boolean isDynamicSize();

    IWidgetGroup setDynamicSize(boolean isDynamicSize);

    @Override
    IWidgetGroup setPos(int x, int y);

    @Override
    IWidgetGroup setSize(int width, int height);

    @Override
    IWidgetGroup setBounds(int x, int y, int width, int height);

    IWidgetGroup addWidget(IGuiWidget widget);

    IWidgetGroup insertWidget(int index, IGuiWidget widget);

    boolean contains(IGuiWidget widget);

    default void addAll(List<IGuiWidget> components) {
        for (IGuiWidget component : components) {
            addWidget(component);
        }
    }

    void remove(IGuiWidget widget);

    void clear();

    @Override
    IWidgetGroup setParent(@Nullable IWidgetGroup parent);

    IGuiWidget get(int index);

    List<IGuiWidget> getChildren();

    @Override
    default void update() {
        for (IGuiWidget widget : getChildren()) {
            widget.update();
        }
    }

    @Override
    IWidgetGroup setBackground(IGuiTexture backgroundTexture);

    @Override
    IWidgetGroup setHoverTexture(IGuiTexture hoverTexture);

    /**
     * @param mouseX  The mouse x position
     * @param mouseY  The mouse y position
     * @param isInner Whether to get the internal component, which will not contain the child components.
     * @return The component at the position.
     */
    default Optional<? extends IGuiWidget> getComponentAt(double mouseX, double mouseY, boolean isInner) {
        for (IGuiWidget component : getChildren()) {
            if (component.isMouseOver(mouseX, mouseY)) {
                if (isInner && component instanceof IWidgetGroup) {
                    return ((IWidgetGroup) component).getComponentAt(mouseX, mouseY, true);
                }
                return Optional.of(component);
            }
        }

        if (isMouseOver(mouseX, mouseY))
            return Optional.of(this);

        return Optional.empty();
    }

    default Optional<? extends IGuiWidget> getComponentAt(double mouseX, double mouseY) {
        return getComponentAt(mouseX, mouseY, false);
    }

    @Override
    default void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        {
            poseStack.translate(this.getX(), this.getY(), 0);
            int translatedX = mouseX - this.getX();
            int translatedY = mouseY - this.getY();
            for (IGuiWidget component : getChildren()) {
                component.render(poseStack, translatedX, translatedY, partialTick);
            }
        }
        poseStack.popPose();
    }

    /**
     * layout all widgets in this group.
     */
    default IWidgetGroup layout() {
        return this;
    }

    default IWidgetGroup addGroupChangedListener(IGroupChangedListener listener) {
        return this;
    }

    default IWidgetGroup removeGroupChangedListener(IGroupChangedListener listener) {
        return this;
    }

    default void notifyGroupChanged(boolean remove, IWidgetGroup group, IGuiWidget widget) {

    }

    interface IGroupChangedListener {
        void onGroupChanged(boolean remove, IWidgetGroup group, IGuiWidget widget);
    }


}
