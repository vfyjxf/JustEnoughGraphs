package com.github.vfyjxf.justenoughgraphs.gui.components;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGraphGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * A component that is not visible and does not handle any input.
 */
public class PointComponent implements IElkGraphComponent {

    private IElkGraphComponent parent;
    private final ElkNode node;

    public PointComponent(@Nullable IElkGraphComponent parent) {
        this.parent = parent;
        this.node = ElkGraphUtil.createNode(parent == null ? null : parent.getNode());
        if (parent != null) parent.add(this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setParent(@Nullable IGraphGroup<?> parent) {
        if (parent == null) return;
        if (!(parent instanceof IElkGraphComponent)) {
            throw new IllegalArgumentException("Parent must be an ElkComponent");
        }
        this.parent = (IElkGraphComponent) parent;
        this.node.setParent(this.parent.getNode());
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
    public ElkNode getNode() {
        return node;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public @Nullable IElkGraphComponent getParent() {
        return parent;
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
        throw new UnsupportedOperationException("PointComponent does not have children");
    }

    @Override
    public List<IElkGraphComponent> getChildren() {
        return Collections.emptyList();
    }
}
