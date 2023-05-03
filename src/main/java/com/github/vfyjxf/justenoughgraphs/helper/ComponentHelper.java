package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;

import java.util.List;

public class ComponentHelper {


    public static List<IGuiWidget> getAllComponents(IWidgetGroup group, List<IGuiWidget> components) {
        for (IGuiWidget child : group.getChildren()) {
            if (child instanceof IWidgetGroup graphGroup) {
                components.addAll(getAllComponents(graphGroup, components));
            } else {
                components.add(child);
            }
        }
        return components;
    }

    public static void moveToTopLeft(IGuiWidget component) {
        moveToTopLeft(component, 0, 0);
    }

    public static void moveToTopLeft(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        component.setPos(xPadding, yPadding);
    }

    public static void moveToTopCenter(IGuiWidget component) {
        moveToTopCenter(component, 0, 0);
    }

    public static void moveToTopCenter(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        int centerX = parent.getWidth() / 2;
        int offsetX = centerX - component.getWidth() / 2;
        component.setPos(offsetX + xPadding, yPadding);
    }

    public static void moveToTopRight(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;

        int rightX = parent.getWidth();
        int offsetX = rightX - component.getWidth();
        //move
        component.setPos(offsetX + xPadding, yPadding);
    }

    public static void moveToCenterLeft(IGuiWidget component) {
        moveToCenterLeft(component, 0, 0);
    }

    public static void moveToCenterLeft(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        int centerY = parent.getHeight() / 2;
        int offsetY = centerY - component.getHeight() / 2;
        component.setPos(xPadding, offsetY + yPadding);
    }

    public static void moveToCenter(IGuiWidget component) {
        moveToCenter(component, 0, 0);
    }

    public static void moveToCenter(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;

        int centerX = parent.getWidth() / 2;
        int centerY = parent.getHeight() / 2;

        int offsetX = centerX - component.getWidth() / 2;
        int offsetY = centerY - component.getHeight() / 2;

        component.setPos(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToCenterRight(IGuiWidget component) {
        moveToCenterRight(component, 0, 0);
    }

    public static void moveToCenterRight(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;

        int rightX = parent.getWidth();
        int centerY = parent.getHeight() / 2;

        int offsetX = rightX - component.getWidth();
        int offsetY = centerY - component.getHeight() / 2;

        component.setPos(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToBottomLeft(IGuiWidget component) {
        moveToBottomLeft(component, 0, 0);
    }

    public static void moveToBottomLeft(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        int bottomY = parent.getHeight();
        int offsetY = bottomY - component.getHeight();
        component.setPos(xPadding, offsetY + yPadding);
    }

    public static void moveToBottomCenter(IGuiWidget component) {
        moveToBottomCenter(component, 0, 0);
    }

    public static void moveToBottomCenter(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        int centerX = parent.getWidth() / 2;
        int bottomY = parent.getHeight();
        int offsetX = centerX - component.getWidth() / 2;
        int offsetY = bottomY - component.getHeight();
        component.setPos(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToBottomRight(IGuiWidget component) {
        moveToBottomRight(component, 0, 0);
    }

    public static void moveToBottomRight(IGuiWidget component, int xPadding, int yPadding) {
        IGuiWidget parent = component.getParent();
        if (parent == null) return;
        int rightX = parent.getWidth();
        int bottomY = parent.getHeight();
        int offsetX = rightX - component.getWidth();
        int offsetY = bottomY - component.getHeight();
        component.setPos(offsetX + xPadding, offsetY + yPadding);
    }

}
