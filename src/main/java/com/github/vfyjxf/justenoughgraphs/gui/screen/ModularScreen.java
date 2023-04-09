package com.github.vfyjxf.justenoughgraphs.gui.screen;

import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.InputTypes;
import com.github.vfyjxf.justenoughgraphs.gui.IComponentFactory;
import com.github.vfyjxf.justenoughgraphs.gui.input.InputContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ModularScreen extends Screen {

    protected final IComponentFactory componentFactory;

    protected IElkGraphComponent screen;
    protected IElkGraphComponent components;

    protected ModularScreen(IComponentFactory componentFactory) {
        super(Component.empty());
        this.componentFactory = componentFactory;
        this.screen = componentFactory.createComponentGroup(null);
        this.components = componentFactory.createComponentGroup(screen);
    }

    public void init() {

    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        for (IElkGraphComponent component : screen.getChildren()) {
            component.render(poseStack, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        for (IElkGraphComponent component : screen.getChildren()) {
            if (component.isMouseOver(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        screen.update();
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        boolean handled = super.keyPressed(keycode, scancode, modifiers);
        if (handled) return true;
        InputContext context = InputContext.fromVanilla(keycode, scancode, modifiers, InputTypes.IMMEDIATE);
        return screen.handleKeyTyped(this, context);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        InputContext context = InputContext.fromVanilla(mouseX, mouseY, button, InputTypes.IMMEDIATE);
        boolean handled = context != null && screen.handleMouseClick(this, context);
        return handled || super.mouseClicked(mouseX, mouseY, button);
    }


    public @NotNull Optional<? extends IElkGraphComponent> getComponentAt(double pMouseX, double pMouseY) {
        return (Optional<? extends IElkGraphComponent>) screen.getComponentAt(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

}
