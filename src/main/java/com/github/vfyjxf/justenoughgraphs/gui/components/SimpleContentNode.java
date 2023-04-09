package com.github.vfyjxf.justenoughgraphs.gui.components;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.gui.IGraphGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.gui.RenderHelper;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * An abstract implementation of ContentNode, it should not have child nodes.
 */
public class SimpleContentNode<T> implements IContentNode<T> {

    protected final ElkNode node;

    protected IContent<T> content;
    protected IContentHelper<T> contentHelper;
    protected IContentRenderer<T> contentRenderer;
    @Nullable
    protected IElkGraphComponent parent;
    protected boolean visible = true;

    public SimpleContentNode(@Nullable IElkGraphComponent parent, IContent<T> content) {
        this.parent = parent;
        this.node = ElkGraphUtil.createNode(parent == null ? null : parent.getNode());
        if (parent != null) parent.add(this);
        this.content = content;
        IRegistryManager registryManager = IRegistryManager.getInstance();
        this.contentHelper = registryManager.getContentHelper(content.getType());
        this.contentRenderer = registryManager.getContentRenderer(content.getType());
        this.node.setWidth(this.contentRenderer.getWidth() + 2);
        this.node.setHeight(this.contentRenderer.getHeight() + 2);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setParent(@Nullable IGraphGroup<?> parent) {
        if (parent == null) {
            this.parent = null;
            this.node.setParent(null);
            return;
        }
        if (!(parent instanceof IElkGraphComponent)) {
            throw new IllegalArgumentException("Parent must be an ElkComponent");
        }
        this.parent = (IElkGraphComponent) parent;
        this.node.setParent(this.parent.getNode());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        int x = this.getX();
        int y = this.getY();
        int width = this.getWidth();
        int height = this.getHeight();
//        DrawHelper.drawLine(poseStack, x, y, x + width, y, 0xFF000000, 0.45F);
//        DrawHelper.drawLine(poseStack, x, y, x, y + height, 0xFF000000, 0.45F);
//        DrawHelper.drawLine(poseStack, x + width, y, x + width, y + height, 0xFF000000, 0.45F);
//        DrawHelper.drawLine(poseStack, x, y + height, x + width, y + height, 0xFF000000, 0.45F);
        RenderHelper.vLine(poseStack, x, y, y + height, 0xFF000000);
        RenderHelper.vLine(poseStack, x + width, y, y + height, 0xFF000000);
        RenderHelper.hLine(poseStack, x, x + width, y, 0xFF000000);
        RenderHelper.hLine(poseStack, x, x + width, y + height, 0xFF000000);
        poseStack.pushPose();
        {
            poseStack.translate(x + 1, y + 1, 0);
            contentRenderer.render(poseStack, content.getContent());
        }
        poseStack.popPose();
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
        return content;
    }

    @Override
    public IContentNode<T> setContent(IContent<T> content) {
        this.content = content;
        return this;
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
    public void add(IElkGraphComponent component) {
        //NOOP
    }

    @Override
    public void remove(IElkGraphComponent component) {
        //NOOP
    }

    @Override
    public void clear() {
        //NOOP
    }

    @Override
    public IElkGraphComponent get(int index) {
        throw new UnsupportedOperationException("This node has no children");
    }

    @Override
    public List<IElkGraphComponent> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "ContentNode{" +
                "node=" + node +
                ", content=" + content.getContent() +
                ", visible=" + visible +
                '}';
    }
}
