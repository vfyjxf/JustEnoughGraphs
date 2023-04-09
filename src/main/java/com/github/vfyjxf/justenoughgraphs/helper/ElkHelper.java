package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.google.common.collect.Iterators;
import org.eclipse.elk.graph.ElkNode;

import java.util.Iterator;

public class ElkHelper {

    public static Iterator<ElkNode> getAllElkNodes(final ElkNode parentNode) {
        return Iterators.filter(parentNode.eAllContents(), ElkNode.class);
    }

    public static <T> Iterator<T> getAllContent(final ElkNode parentNode, final Class<T> clazz) {
        return Iterators.filter(parentNode.eAllContents(), clazz);
    }

    public static void moveToTopLeft(IElkGraphComponent component) {
        moveToTopLeft(component, 0, 0);
    }

    public static void moveToTopLeft(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        //移动
        component.getNode().setLocation(xPadding, yPadding);
    }

    public static void moveToTopCenter(IElkGraphComponent component) {
        moveToTopCenter(component, 0, 0);
    }

    public static void moveToTopCenter(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        double centerX = parent.getNode().getWidth() / 2;
        double offsetX = centerX - component.getNode().getWidth() / 2;
        component.getNode().setLocation(offsetX + xPadding, yPadding);
    }

    public static void moveToTopRight(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        //top right
        double rightX = parent.getNode().getWidth();
        //padding
        double offsetX = rightX - component.getNode().getWidth();
        //move
        component.getNode().setLocation(offsetX + xPadding, yPadding);
    }

    public static void moveToCenterLeft(IElkGraphComponent component) {
        moveToCenterLeft(component, 0, 0);
    }

    public static void moveToCenterLeft(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        double centerY = parent.getNode().getHeight() / 2;
        double offsetY = centerY - component.getNode().getHeight() / 2;
        component.getNode().setLocation(xPadding, offsetY);
    }

    public static void moveToCenter(IElkGraphComponent component) {
        moveToCenter(component, 0, 0);
    }

    public static void moveToCenter(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        //计算中心点
        double centerX = parent.getNode().getWidth() / 2;
        double centerY = parent.getNode().getHeight() / 2;
        //计算偏移量
        double offsetX = centerX - component.getNode().getWidth() / 2;
        double offsetY = centerY - component.getNode().getHeight() / 2;
        //移动
        component.getNode().setLocation(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToCenterRight(IElkGraphComponent component) {
        moveToCenterRight(component, 0, 0);
    }

    public static void moveToCenterRight(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;

        double rightX = parent.getNode().getWidth();
        double centerY = parent.getNode().getHeight() / 2;

        double offsetX = rightX - component.getNode().getWidth();
        double offsetY = centerY - component.getNode().getHeight() / 2;

        component.getNode().setLocation(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToBottomLeft(IElkGraphComponent component) {
        moveToBottomLeft(component, 0, 0);
    }

    public static void moveToBottomLeft(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        double bottomY = parent.getNode().getHeight();
        double offsetY = bottomY - component.getNode().getHeight();
        component.getNode().setLocation(xPadding, offsetY + yPadding);
    }

    public static void moveToBottomCenter(IElkGraphComponent component) {
        moveToBottomCenter(component, 0, 0);
    }

    public static void moveToBottomCenter(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        double centerX = parent.getNode().getWidth() / 2;
        double bottomY = parent.getNode().getHeight();
        double offsetX = centerX - component.getNode().getWidth() / 2;
        double offsetY = bottomY - component.getNode().getHeight();
        component.getNode().setLocation(offsetX + xPadding, offsetY + yPadding);
    }

    public static void moveToBottomRight(IElkGraphComponent component) {
        moveToBottomRight(component, 0, 0);
    }

    public static void moveToBottomRight(IElkGraphComponent component, double xPadding, double yPadding) {
        IElkGraphComponent parent = component.getParent();
        if (parent == null) return;
        double rightX = parent.getNode().getWidth();
        double bottomY = parent.getNode().getHeight();
        double offsetX = rightX - component.getNode().getWidth();
        double offsetY = bottomY - component.getNode().getHeight();
        component.getNode().setLocation(offsetX + xPadding, offsetY + yPadding);
    }


}
