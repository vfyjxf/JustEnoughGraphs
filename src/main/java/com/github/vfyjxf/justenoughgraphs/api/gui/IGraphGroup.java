package com.github.vfyjxf.justenoughgraphs.api.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.List;
import java.util.Optional;

public interface IGraphGroup<T extends IGraphComponent> extends IGraphComponent {

    void add(T component);

    default void addAll(List<T> components) {
        for (T component : components) {
            add(component);
        }
    }

    void remove(T component);

    void clear();

    T get(int index);

    List<T> getChildren();

    @Override
    default void update() {
        for (T component : getChildren()) {
            component.update();
        }
    }

    /**
     * @param mouseX  The mouse x position
     * @param mouseY  The mouse y position
     * @param isInner Whether to get the internal component, which will not contain the child components.
     * @return The component at the position.
     */
    default Optional<? extends IGraphComponent> getComponentAt(double mouseX, double mouseY, boolean isInner) {
        for (T component : getChildren()) {
            if (component.isMouseOver(mouseX, mouseY)) {
                if (isInner && component instanceof IGraphGroup) {
                    return ((IGraphGroup<T>) component).getComponentAt(mouseX, mouseY, true);
                }
                return Optional.of(component);
            }
        }

        if (isMouseOver(mouseX, mouseY))
            return Optional.of(this);

        return Optional.empty();
    }

    default Optional<? extends IGraphComponent> getComponentAt(double mouseX, double mouseY) {
        return getComponentAt(mouseX, mouseY, false);
    }

    @Override
    default void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        {
            poseStack.translate(getX(), getY(), 0);
            for (T component : getChildren()) {
                component.render(poseStack, mouseX, mouseY, partialTick);
            }
        }
        poseStack.popPose();
    }
}
