package com.github.vfyjxf.justenoughgraphs.gui.widgets;

/**
 * A widget that can be rendered in different colours for different themes.
 */
public interface IColoredWidget {

    int getBackgroundColor();

    int getBorderColor();

    IColoredWidget setBackgroundColor(int color);

    IColoredWidget setBorderColor(int color);

    IColoredWidget setBorderWidth(int width);

}
