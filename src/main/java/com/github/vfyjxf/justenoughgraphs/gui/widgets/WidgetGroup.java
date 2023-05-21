package com.github.vfyjxf.justenoughgraphs.gui.widgets;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class WidgetGroup extends Widget implements IWidgetGroup {

    private boolean isDynamicSize = false;
    protected final List<IGuiWidget> children = new ArrayList<>();
    protected final List<IGroupChangedListener> listeners = new ArrayList<>();

    public WidgetGroup(
            int x, int y,
            int width, int height,
            @Nullable IWidgetGroup parent,
            @Nullable IGuiTexture background,
            @Nullable IGuiTexture hoverTexture,
            @Nullable BiPredicate<Screen, IInputContext> clickHandler,
            @Nullable BiPredicate<Screen, IInputContext> keyHandler
    ) {
        super(x, y, width, height, parent, background, hoverTexture, clickHandler, keyHandler);
    }

    public WidgetGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public WidgetGroup() {
    }

    @Override
    public boolean isDynamicSize() {
        return this.isDynamicSize;
    }

    @Override
    public IWidgetGroup setDynamicSize(boolean isDynamicSize) {
        this.isDynamicSize = isDynamicSize;
        return this;
    }

    @Override
    public IWidgetGroup setPos(int x, int y) {
        super.setPos(x, y);
        return this;
    }

    @Override
    public IWidgetGroup setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public IWidgetGroup setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        return this;
    }

    @Override
    public IWidgetGroup addWidget(IGuiWidget widget) {
        if (!children.contains(widget)) {
            if (this.addWidget(children.size(), widget))
                recomputeSize();
        }
        return this;
    }

    @Override
    public IWidgetGroup insertWidget(int index, IGuiWidget widget) {
        ErrorChecker.checkRangeClosed(index, children.size());
        this.addWidget(index, widget);
        return this;
    }

    @Override
    public boolean contains(IGuiWidget widget) {
        return children.contains(widget);
    }

    protected boolean addWidget(int index, IGuiWidget widget) {
        if (!children.contains(widget)) {
            children.add(index, widget);
            widget.setParent(this);
            this.notifyGroupChanged(false, this, widget);
            return true;
        }
        return false;
    }

    protected void recomputeSize() {
        //all children must be within the bounds of the parent

        int currentX = getX();
        int currentY = getY();
        int currentWidth = getWidth();
        int currentHeight = getHeight();

        for (IGuiWidget child : children) {
            int childX = child.getX();
            int childY = child.getY();
            int childWidth = child.getWidth();
            int childHeight = child.getHeight();

            if (childX < currentX) {
                currentWidth += currentX - childX;
                currentX = childX;
            }
            if (childY < currentY) {
                currentHeight += currentY - childY;
                currentY = childY;
            }
            if (childX + childWidth > currentX + currentWidth) {
                currentWidth = childX + childWidth - currentX;
            }
            if (childY + childHeight > currentY + currentHeight) {
                currentHeight = childY + childHeight - currentY;
            }
        }
        this.setBounds(currentX, currentY, currentWidth, currentHeight);

    }

    @Override
    public void addAll(List<IGuiWidget> components) {
        for (IGuiWidget component : components) {
            addWidget(component);
        }
    }

    @Override
    public void remove(IGuiWidget widget) {
        if (children.contains(widget)) {
            children.remove(widget);
            widget.setParent(null);
            this.notifyGroupChanged(true, this, widget);
        }
    }

    @Override
    public void clear() {
        for (IGuiWidget child : children) {
            child.setParent(null);
        }
        children.clear();
    }

    @Override
    public IWidgetGroup setParent(@Nullable IWidgetGroup parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public IGuiWidget get(int index) {
        return children.get(index);
    }

    @Override
    public List<IGuiWidget> getChildren() {
        return new ArrayList<>(children);
    }


    @Override
    public Widget setExtraRender(BiConsumer<PoseStack, Widget> extraRender) {
        return super.setExtraRender(extraRender);
    }

    @Override
    public WidgetGroup setBackground(IGuiTexture backgroundTexture) {
        super.setBackground(backgroundTexture);
        return this;
    }

    @Override
    public WidgetGroup setHoverTexture(IGuiTexture hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        {
            poseStack.translate(this.getX(), this.getY(), 0);
            this.renderBackground(poseStack, mouseX, mouseY, partialTick);
            this.renderForeground(poseStack, mouseX, mouseY, partialTick);
            if (extraRender != null) extraRender.accept(poseStack, this);
            int translatedX = mouseX - this.getX();
            int translatedY = mouseY - this.getY();
            for (IGuiWidget child : children) {
                child.render(poseStack, translatedX, translatedY, partialTick);
            }
        }
        poseStack.popPose();
    }

    @Override
    public WidgetGroup addGroupChangedListener(IGroupChangedListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public WidgetGroup removeGroupChangedListener(IGroupChangedListener listener) {
        listeners.remove(listener);
        return this;
    }

    @Override
    public void notifyGroupChanged(boolean remove, IWidgetGroup group, IGuiWidget widget) {
        for (IGroupChangedListener listener : listeners) {
            listener.onGroupChanged(remove, group, widget);
        }
    }

    @Override
    public String toString() {
        return "WidgetGroup{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", width=" + width +
                ", height=" + height +
                ", active=" + active +
                ", visible=" + visible +
                ", focused=" + focused +
                ", dragging=" + dragging +
                '}';
    }
}
