package com.github.vfyjxf.justenoughgraphs.gui;


import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Create different themed components.
 */
public interface IComponentFactory {

    void setProperties(Consumer<IElkGraphComponent> propertiesSetter);

    IElkGraphComponent createComponentGroup(@Nullable IElkGraphComponent parent);

    <T> IContentNode<T> createContentNode(@Nullable IElkGraphComponent parent, IContent<T> content);

    default <T> IContentNode<T> createContentNode(IContent<T> content) {
        return createContentNode(null, content);
    }

    <T> IContentNode<T> createEditableNode(@Nullable IElkGraphComponent parent, IContent<T> content);

    IElkGraphComponent createLabelNode(@Nullable IElkGraphComponent parent, String text);

    IElkGraphComponent createPoint(@Nullable IElkGraphComponent parent);

}
