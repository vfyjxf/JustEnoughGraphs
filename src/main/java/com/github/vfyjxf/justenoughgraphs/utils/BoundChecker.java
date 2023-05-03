package com.github.vfyjxf.justenoughgraphs.utils;

import org.eclipse.elk.graph.ElkShape;

public class BoundChecker {

    public static boolean checkOver(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + height;
    }

    public static boolean checkOver(ElkShape shape, double mouseX, double mouseY) {
        return mouseX >= shape.getX() &&
                mouseX <= shape.getX() + shape.getWidth() &&
                mouseY >= shape.getY() &&
                mouseY <= shape.getY() + shape.getHeight();
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

    public static boolean matches(ElkShape shape1, ElkShape shape2) {
        return shape1.getX() == shape2.getX() &&
                shape1.getY() == shape2.getY() &&
                shape1.getWidth() == shape2.getWidth() &&
                shape1.getHeight() == shape2.getHeight();
    }


}
