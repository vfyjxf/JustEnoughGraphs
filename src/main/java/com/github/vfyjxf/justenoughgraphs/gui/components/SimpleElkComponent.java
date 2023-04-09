package com.github.vfyjxf.justenoughgraphs.gui.components;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGraphGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.gui.RenderHelper;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleElkComponent implements IElkGraphComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final List<IElkGraphComponent> children;
    protected final ElkNode node;

    @Nullable
    protected IElkGraphComponent parent;
    protected boolean visible = true;

    public SimpleElkComponent(@Nullable IElkGraphComponent parent) {
        this.parent = parent;
        this.node = ElkGraphUtil.createNode(parent == null ? null : parent.getNode());
        this.children = new ArrayList<>();
        if (parent != null) parent.add(this);
    }

    public SimpleElkComponent() {
        this.node = ElkGraphUtil.createGraph();
        this.children = new ArrayList<>();
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        int x = this.getX();
        int y = this.getY();
        int width = this.getWidth();
        int height = this.getHeight();
        RenderHelper.vLine(poseStack, x, y, y + height, 0xFF000000);
        RenderHelper.vLine(poseStack, x + width, y, y + height, 0xFF000000);
        RenderHelper.hLine(poseStack, x, x + width, y, 0xFF000000);
        RenderHelper.hLine(poseStack, x, x + width, y + height, 0xFF000000);
        poseStack.pushPose();
        {
            poseStack.translate(x, y, 0);
            for (IElkGraphComponent component : getChildren()) {
                component.render(poseStack, mouseX, mouseY, partialTick);
            }
        }
        poseStack.popPose();
    }

    @Override
    public boolean handleMouseClick(@Nullable Screen screen, IInputContext context) {
        for (IElkGraphComponent child : children) {
            if (child.handleMouseClick(screen, context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleKeyTyped(@Nullable Screen screen, IInputContext context) {
        for (IElkGraphComponent child : children) {
            if (child.handleKeyTyped(screen, context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ElkNode getNode() {
        return node;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return BoundChecker.checkOver(node, mouseX, mouseY);
    }

    @Override
    public @Nullable IElkGraphComponent getParent() {
        return parent;
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
    public void add(IElkGraphComponent component) {
        if (children.contains(component)) {
            LOGGER.warn("Component {} already exists in this group.", component);
            return;
        }
        children.add(component);
        component.setParent(this);
    }

    @Override
    public void remove(IElkGraphComponent component) {
        if (!children.contains(component)) {
            LOGGER.warn("Component {} does not exist in this group.", component);
            return;
        }
        children.remove(component);
        component.setParent(null);
    }

    @Override
    public void clear() {
        children.clear();
    }

    @Override
    public IElkGraphComponent get(int index) {
        return children.get(index);
    }

    @Override
    public List<IElkGraphComponent> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public String toString() {
        return "ElkComponent{" +
                "children=" + children +
                ", node=" + node +
                ", visible=" + visible +
                '}';
    }
}
