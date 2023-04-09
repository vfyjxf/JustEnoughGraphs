package com.github.vfyjxf.justenoughgraphs.api.gui.elk;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGraphGroup;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.emf.common.util.EList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IElkGraphComponent extends IGraphGroup<IElkGraphComponent> {

    /**
     * The ElkNode that this component is based on.
     * It contains all the information about the component such as position, size, ports, etc.
     *
     * @return the ElkNode that this component is based on.
     */
    ElkNode getNode();

    @Override
    default int getX() {
        return (int) getNode().getX();
    }

    @Override
    default int getY() {
        return (int) getNode().getY();
    }

    @Override
    default int getWidth() {
        return (int) getNode().getWidth();
    }

    @Override
    default int getHeight() {
        return (int) getNode().getHeight();
    }

    @Override
    default void setPos(int x, int y) {
        getNode().setLocation(x, y);
    }

    @Override
    default void resize(int width, int height) {
        getNode().setWidth(width);
        getNode().setHeight(height);
    }

    @Override
    default void setBounds(int x, int y, int width, int height) {
        setPos(x, y);
        resize(width, height);
    }

    @Override
    boolean isMouseOver(double mouseX, double mouseY);

    @Override
    @Nullable IElkGraphComponent getParent();

    @Override
    void add(IElkGraphComponent component);

    @Override
    void remove(IElkGraphComponent component);

    @Override
    void clear();

    @Override
    IElkGraphComponent get(int index);

    @Override
    List<IElkGraphComponent> getChildren();

    default EList<ElkPort> getPorts() {
        return getNode().getPorts();
    }

    default EList<ElkEdge> getContainedEdges() {
        return getNode().getContainedEdges();
    }

}
