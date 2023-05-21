package com.github.vfyjxf.justenoughgraphs.api.gui;

import org.jetbrains.annotations.Nullable;

public interface IDraggableWidget {

    /**
     * @param mouseX      mouse x position
     * @param mouseY      mouse y position
     * @param underWidget the widget under the mouse.
     * @return true if the widget can be dragged to current
     */
    default boolean isAccept(double mouseX, double mouseY, @Nullable IGuiWidget underWidget) {
        return false;
    }

    boolean isDragging();

    void setDragging(boolean dragging);

    default void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {

    }
}
