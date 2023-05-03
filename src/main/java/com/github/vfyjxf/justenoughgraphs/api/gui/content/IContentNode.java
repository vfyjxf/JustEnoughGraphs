package com.github.vfyjxf.justenoughgraphs.api.gui.content;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.Nullable;

public interface IContentNode<T> extends IGuiWidget {

    IContent<T> getContent();

    @CanIgnoreReturnValue
    <V> IContentNode<V> setContent(IContent<V> content);

    @Override
    IContentNode<T> setPos(int x, int y);

    @Override
    IContentNode<T> resize(int width, int height);

    @Override
    IContentNode<T> setBounds(int x, int y, int width, int height);

    @Override
    void setActive(boolean active);

    @Override
    void setVisible(boolean visible);

    @Override
    void setFocused(boolean focused);

    @Override
    void setDragging(boolean dragging);

    @Override
    IContentNode<T> setParent(@Nullable IWidgetGroup parent);

    @Override
    IContentNode<T> setBackground(IGuiTexture backgroundTexture);

    @Override
    IContentNode<T> setHoverTexture(IGuiTexture hoverTexture);
}
