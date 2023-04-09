package com.github.vfyjxf.justenoughgraphs.gui.input;

import com.github.vfyjxf.justenoughgraphs.api.gui.input.IInputContext;
import com.github.vfyjxf.justenoughgraphs.api.gui.input.InputTypes;
import com.github.vfyjxf.justenoughgraphs.utils.InputHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;

public class InputContext implements IInputContext {

    public static InputContext fromVanilla(int keyCode, int scanCode, int modifiers, InputTypes inputType) {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        return new InputContext(key, InputHelper.getMouseX(), InputHelper.getMouseY(), modifiers, inputType);
    }

    public static InputContext fromVanilla(double mouseX, double mouseY, int mouseButton, InputTypes inputType) {
        if (mouseButton < 0) {
            return null;
        }
        InputConstants.Key button = InputConstants.Type.MOUSE.getOrCreate(mouseButton);
        return new InputContext(button, InputHelper.getMouseX(), InputHelper.getMouseY(), 0, inputType);
    }


    private final InputConstants.Key key;
    private final double mouseX;
    private final double mouseY;
    private final int modifiers;
    private final InputTypes clickState;

    public InputContext(InputConstants.Key key, double mouseX, double mouseY, int modifiers, InputTypes clickState) {
        this.key = key;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.modifiers = modifiers;
        this.clickState = clickState;
    }

    @Override
    public InputConstants.Key getKey() {
        return key;
    }

    @Override
    public double getMouseX() {
        return mouseX;
    }

    @Override
    public double getMouseY() {
        return mouseY;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public InputTypes getClickState() {
        return clickState;
    }

    @Override
    public boolean isAllowedChatCharacter() {
        return isKeyboard() && SharedConstants.isAllowedChatCharacter((char) this.key.getValue());
    }

    @Override
    public boolean is(KeyMapping keyMapping) {
        return keyMapping.isActiveAndMatches(this.key);
    }

}
