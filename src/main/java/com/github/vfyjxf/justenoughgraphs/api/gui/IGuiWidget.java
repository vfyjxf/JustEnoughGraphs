package com.github.vfyjxf.justenoughgraphs.api.gui;

import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.texture.IGuiTexture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An interface representing a gui component whose coordinates are referenced to the parent component.
 */
public interface IGuiWidget extends Widget, GuiEventListener, IDraggableWidget {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    List<Component> getTooltips();

    default List<ClientTooltipComponent> getClientTooltips() {
        return getTooltips()
                .stream()
                .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                .collect(Collectors.toList());
    }

    @CanIgnoreReturnValue
    IGuiWidget setPos(int x, int y);

    @CanIgnoreReturnValue
    IGuiWidget setSize(int width, int height);

    @CanIgnoreReturnValue
    IGuiWidget setBounds(int x, int y, int width, int height);

    boolean isMouseOver(double mouseX, double mouseY);

    boolean isActive();

    void setActive(boolean active);

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isFocused();

    void setFocused(boolean focused);

    @Nullable
    IWidgetGroup getParent();

    @CanIgnoreReturnValue
    IGuiWidget setParent(@Nullable IWidgetGroup parent);

    default IGuiWidget setChildOf(IWidgetGroup parent) {
        this.setParent(parent);
        parent.addWidget(this);
        return this;
    }

    default void update() {

    }

    @CanIgnoreReturnValue
    IGuiWidget setBackground(IGuiTexture backgroundTexture);

    @CanIgnoreReturnValue
    IGuiWidget setHoverTexture(IGuiTexture hoverTexture);

    @Override
    void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick);

    boolean handleMouseClick(@Nullable Screen screen, IInputContext context);

    boolean handleKeyTyped(@Nullable Screen screen, IInputContext context);

}
