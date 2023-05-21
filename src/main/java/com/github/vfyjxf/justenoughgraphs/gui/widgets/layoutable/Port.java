package com.github.vfyjxf.justenoughgraphs.gui.widgets.layoutable;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.ILayoutableGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.Widget;
import com.github.vfyjxf.justenoughgraphs.helper.RenderHelper;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.github.vfyjxf.justenoughgraphs.utils.ColorUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.PortSide;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.elk.graph.properties.IProperty;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

public class Port extends Widget {

    public static Port createPort(ILayoutableGroup graph, IGuiWidget parent, int width, int height, int color) {
        ElkNode parentNode = graph.getNode(parent);
        if (parentNode == null) {
            throw new IllegalArgumentException("Parent node not found");
        }
        ElkPort self = ElkGraphUtil.createPort(parentNode);
        Port port = new Port(parent, self, color).setSize(width, height);
        graph.addWidget(port);
        return port;
    }

    public static Port createPort(ILayoutableGroup graph, IGuiWidget parent) {
        return createPort(graph, parent, 2, 2, ColorUtils.WHITE);
    }

    public static Port createPort(ILayoutableGroup graph, IGuiWidget parent, int color) {
        return createPort(graph, parent, 3, 3, color);
    }

    private final ElkPort self;
    private IGuiWidget parent;
    private boolean visible = true;
    private boolean focused = false;
    private int color;

    private Port(IGuiWidget parent, ElkPort self, int color) {
        this.parent = parent;
        this.self = self;
        this.color = color;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return BoundChecker.checkOver(self, mouseX, mouseY);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {

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
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * @return the parent group of the node to which this Port belongs.
     */
    @Override
    public @Nullable IWidgetGroup getParent() {
        return parent.getParent();
    }

    public ElkPort self() {
        return self;
    }

    @Override
    public Port setPos(int x, int y) {
        super.setPos(x, y);
        this.self.setLocation(x, y);
        return this;
    }

    @Override
    public Port setSize(int width, int height) {
        super.setSize(width, height);
        this.self.setWidth(width);
        this.self.setHeight(height);
        return this;
    }

    @Override
    public Port setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.setPos(x, y);
        this.setSize(width, height);
        return this;
    }

    @Override
    public Port setBackground(IGuiTexture backgroundTexture) {
        return this;
    }

    @Override
    public Port setHoverTexture(IGuiTexture hoverTexture) {
        return this;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        PortSide side = self.getProperty(CoreOptions.PORT_SIDE);
        //Offset by one pixel in the specified direction.
        int xPadding = side == PortSide.WEST ? -1 : side == PortSide.EAST ? 1 : 0;
        int yPadding = side == PortSide.SOUTH ? -1 : side == PortSide.NORTH ? 1 : 0;
        RenderHelper.drawSolidRect(poseStack, this.getX() + xPadding, getY() + yPadding, getWidth(), getHeight(), color);
    }

    public <T> Port setProperty(IProperty<? super T> property, T value) {
        self.setProperty(property, value);
        return this;
    }

    public <T> T getProperty(IProperty<T> property) {
        return self.getProperty(property);
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
    public String toString() {
        return "Port{" +
                "self=" + self +
                ", visible=" + visible +
                ", focused=" + focused +
                ", color=" + color +
                '}';
    }
}
