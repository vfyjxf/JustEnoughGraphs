package com.github.vfyjxf.justenoughgraphs.api.gui.input;

public enum InputTypes {
    /**
     * called on mouse-down or to see if a click would be handled
     */
    SIMULATE,
    /**
     * called on mouse-up after a successful {@link InputTypes#SIMULATE}
     */
    EXECUTE,
    /**
     * called on key-down, or mouse-down to execute a click from a vanilla GUI without waiting for mouse-up
     */
    IMMEDIATE,
}
