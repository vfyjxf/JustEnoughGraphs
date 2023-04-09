package com.github.vfyjxf.justenoughgraphs.api.gui;

import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface IGraphComponent extends Widget, GuiEventListener {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setPos(int x, int y);

    void resize(int width, int height);

    void setBounds(int x, int y, int width, int height);

    boolean isMouseOver(double mouseX, double mouseY);

    boolean isVisible();

    void setVisible(boolean visible);

    @Nullable
    IGraphGroup<?> getParent();

    void setParent(@Nullable IGraphGroup<?> parent);

    default void update() {

    }

    @Override
    void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick);

    boolean handleMouseClick(@Nullable Screen screen, IInputContext context);

    boolean handleKeyTyped(@Nullable Screen screen, IInputContext context);

}
