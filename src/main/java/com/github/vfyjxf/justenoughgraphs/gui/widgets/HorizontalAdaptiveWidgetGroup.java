package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;

/**
 * A WidgetGroup that will automatically arrange the internal components into rectangles of size m * n.
 * Each subcomponent does not necessarily require the same dimensions,
 * but this may result in a strange visual presentation.
 */
public class HorizontalAdaptiveWidgetGroup extends WidgetGroup {


    @Override
    protected boolean addWidget(int index, IGuiWidget widget) {
        if (super.addWidget(index, widget)) {
            int lastPosX = 0;
            int lastWidth = 0;
            if (index > 0) {
                IGuiWidget lastWidget = children.get(index - 1);
                lastPosX = lastWidget.getX();
                lastWidth = lastWidget.getWidth();
            }
            widget.setPos(lastPosX + lastWidth + 1, 0);
        }
        return false;
    }

}
