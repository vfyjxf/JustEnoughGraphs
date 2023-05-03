package com.github.vfyjxf.justenoughgraphs.api.gui;

import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.properties.IProperty;

public interface ILayoutableGroup extends IWidgetGroup {
    ElkNode getInternal();

    ElkNode getNode(IGuiWidget widget);

//    @Nullable
//    ElkPort getPort(IGuiWidget component);

    default <T> ILayoutableGroup setProperty(IProperty<? super T> property, T value) {
        getInternal().setProperty(property, value);
        return this;
    }

    default <T> T getProperty(IProperty<T> property) {
        return getInternal().getProperty(property);
    }

    void layout(IGraphLayoutEngine engine);
}
