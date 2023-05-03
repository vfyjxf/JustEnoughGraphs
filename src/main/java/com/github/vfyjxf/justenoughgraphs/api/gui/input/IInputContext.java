package com.github.vfyjxf.justenoughgraphs.api.gui.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

/**
 * Based {@link mezz.jei.gui.input.UserInput}
 */
public interface IInputContext {

    InputConstants.Key getKey();

    double getMouseX();

    double getMouseY();

    int getModifiers();

    InputType getClickState();

    default boolean isSimulate() {
        return getClickState() == InputType.SIMULATE;
    }

    default boolean isMouse() {
        return this.getKey().getType() == InputConstants.Type.MOUSE;
    }

    default boolean isKeyboard() {
        return this.getKey().getType() == InputConstants.Type.KEYSYM;
    }

    boolean isAllowedChatCharacter();

    boolean is(KeyMapping keyMapping);

}
