package com.github.vfyjxf.justenoughgraphs.gui.factories;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.gui.IComponentFactory;
import com.github.vfyjxf.justenoughgraphs.gui.components.PointComponent;
import com.github.vfyjxf.justenoughgraphs.gui.components.SimpleContentNode;
import com.github.vfyjxf.justenoughgraphs.gui.components.SimpleEditableContentNode;
import com.github.vfyjxf.justenoughgraphs.gui.components.SimpleElkComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SimpleComponentFactory implements IComponentFactory {

    private Consumer<IElkGraphComponent> propertiesSetter;

    @Override
    public void setProperties(Consumer<IElkGraphComponent> propertiesSetter) {
        this.propertiesSetter = propertiesSetter;
    }

    @Override
    public IElkGraphComponent createComponentGroup(@Nullable IElkGraphComponent parent) {
        IElkGraphComponent component = new SimpleElkComponent(parent);
        if (propertiesSetter != null) {
            propertiesSetter.accept(component);
        }
        return component;
    }

    @Override
    public <T> IContentNode<T> createContentNode(@Nullable IElkGraphComponent parent, IContent<T> content) {
        IContentNode<T> contentNode = new SimpleContentNode<>(parent, content);
        if (propertiesSetter != null) {
            propertiesSetter.accept(contentNode);
        }
        return contentNode;
    }

    @Override
    public <T> IContentNode<T> createEditableNode(@Nullable IElkGraphComponent parent, IContent<T> content) {
        IContentNode<T> contentNode = new SimpleEditableContentNode<>();
        if (propertiesSetter != null) {
            propertiesSetter.accept(contentNode);
        }
        return contentNode;
    }

    @Override
    public IElkGraphComponent createLabelNode(@Nullable IElkGraphComponent parent, String text) {
        return null;
    }

    @Override
    public IElkGraphComponent createPoint(@Nullable IElkGraphComponent parent) {
        IElkGraphComponent point = new PointComponent(parent);
        if (propertiesSetter != null) {
            propertiesSetter.accept(point);
        }
        return point;
    }
}
