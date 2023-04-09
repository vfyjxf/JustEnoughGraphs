package com.github.vfyjxf.justenoughgraphs.utils;

import org.eclipse.elk.graph.ElkNode;

public class BoundChecker {

    public static boolean checkOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + height;
    }

    public static boolean checkOver(ElkNode node, double mouseX, double mouseY) {
        return mouseX >= node.getX() &&
                mouseX <= node.getX() + node.getWidth() &&
                mouseY >= node.getY() &&
                mouseY <= node.getY() + node.getHeight();
    }

    public static boolean checkOver(Rect bound, double mouseX, double mouseY) {
        return mouseX >= bound.left &&
                mouseX <= bound.right &&
                mouseY >= bound.up &&
                mouseY <= bound.down;
    }

    public static boolean matches(Rect bound1, Rect bound2) {
        return bound1.left == bound2.left &&
                bound1.right == bound2.right &&
                bound1.up == bound2.up &&
                bound1.down == bound2.down;
    }

    public static boolean matches(ElkNode node1, ElkNode node2) {
        return node1.getX() == node2.getX() &&
                node1.getY() == node2.getY() &&
                node1.getWidth() == node2.getWidth() &&
                node1.getHeight() == node2.getHeight();
    }


}
