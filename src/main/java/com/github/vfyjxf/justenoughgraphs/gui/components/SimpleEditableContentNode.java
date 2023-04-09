package com.github.vfyjxf.justenoughgraphs.gui.components;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.IGraphGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.graph.ElkNode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleEditableContentNode<T> implements IContentNode<T> {
    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setParent(@Nullable IGraphGroup<?> parent) {

    }

    @Override
    public boolean handleMouseClick(@Nullable Screen screen, IInputContext context) {
        return false;
    }

    @Override
    public boolean handleKeyTyped(@Nullable Screen screen, IInputContext context) {
        return false;
    }

    @Override
    public IContent<T> getContent() {
        return null;
    }

    @Override
    public IContentNode<T> setContent(IContent<T> content) {
        return null;
    }

    @Override
    public ElkNode getNode() {
        return null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public @Nullable IElkGraphComponent getParent() {
        return null;
    }

    @Override
    public void add(IElkGraphComponent component) {

    }

    @Override
    public void remove(IElkGraphComponent component) {

    }

    @Override
    public void clear() {

    }

    @Override
    public IElkGraphComponent get(int index) {
        return null;
    }

    @Override
    public List<IElkGraphComponent> getChildren() {
        return null;
    }
}
