package com.github.vfyjxf.justenoughgraphs.gui.widgets.layoutable;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.ILayoutableGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.WidgetGroup;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class LayoutableWidgetGroup extends WidgetGroup implements ILayoutableGroup {

    protected final ElkNode self;
    protected final Map<IGuiWidget, ElkNode> nodeMap;

    public LayoutableWidgetGroup(
            int width, int height,
            @Nullable IWidgetGroup parent,
            @Nullable IGuiTexture background,
            @Nullable IGuiTexture hoverTexture,
            @Nullable BiPredicate<Screen, IInputContext> clickHandler,
            @Nullable BiPredicate<Screen, IInputContext> keyHandler
    ) {
        super(0, 0, 0, 0, parent, background, hoverTexture, clickHandler, keyHandler);
        this.self = ElkGraphUtil.createGraph();
        this.nodeMap = new IdentityHashMap<>();
        this.nodeMap.put(this, self);
        this.setBounds(0, 0, width, height);
    }

    public LayoutableWidgetGroup() {
        this(0, 0, null, null, null, null, null);
    }


    @Override
    public int getX() {
        return (int) self.getX();
    }

    @Override
    public int getY() {
        return (int) self.getY();
    }

    @Override
    public int getWidth() {
        return (int) self.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) self.getHeight();
    }

    @Override
    public ILayoutableGroup setPos(int x, int y) {
        super.setPos(x, y);
        self.setLocation(x, y);
        return this;
    }

    @Override
    public ILayoutableGroup setSize(int width, int height) {
        super.setSize(width, height);
        getInternal().setWidth(width);
        getInternal().setHeight(height);
        return this;
    }

    @Override
    public ILayoutableGroup setBounds(int x, int y, int width, int height) {
        setPos(x, y);
        setSize(width, height);
        return this;
    }

    @Override
    public IWidgetGroup addWidget(IGuiWidget widget) {
        if (!this.addWidget(children.size(), widget))
            return this;

        ElkNode child;
        if (widget instanceof ILayoutableGroup layoutableGroup) {
            child = layoutableGroup.getInternal();
            child.setParent(self);
        } else {
            child = ElkGraphUtil.createNode(self);
        }
        child.setLocation(widget.getX(), widget.getY());
        child.setWidth(widget.getWidth());
        child.setHeight(widget.getHeight());
        nodeMap.put(widget, child);
        return this;
    }

    @Override
    public void remove(IGuiWidget widget) {
        super.remove(widget);
        ElkNode node = nodeMap.get(widget);
        if (node != null) {
            node.setParent(null);
        }
        this.nodeMap.remove(widget);
    }

    @Override
    public void clear() {
        super.clear();
        this.nodeMap.clear();
//        this.portMap.clear();
        this.nodeMap.put(this, self);
    }

    @Override
    public IGuiWidget get(int index) {
        return children.get(index);
    }

    @Override
    public ElkNode getInternal() {
        return self;
    }

    @Override
    public ElkNode getNode(IGuiWidget widget) {
        return nodeMap.get(widget);
    }

    @Override
    public void layout(IGraphLayoutEngine engine) {
        IElkProgressMonitor monitor = new NullElkProgressMonitor();
        //Protected synchronisation,
        // placed after a component has been added without synchronisation of property changes.
        syncData();
        for (Map.Entry<IGuiWidget, ElkNode> node : nodeMap.entrySet()) {
            engine.layout(node.getValue(), monitor);
            if (node.getKey() != this && node.getKey() instanceof ILayoutableGroup layoutableGroup) {
                layoutableGroup.layout(engine);
            }
        }
        nodeMap.forEach((component, node) -> {
            component.setPos((int) node.getX(), (int) node.getY());
            component.setSize((int) node.getWidth(), (int) node.getHeight());
        });
    }

    /**
     * Synchronise the data from the widgets to the nodes.
     */
    private void syncData() {
        for (Map.Entry<IGuiWidget, ElkNode> entry : nodeMap.entrySet()) {
            ElkNode node = entry.getValue();
            IGuiWidget widget = entry.getKey();
            node.setLocation(widget.getX(), widget.getY());
            node.setWidth(widget.getWidth());
            node.setHeight(widget.getHeight());
        }
    }
}
