package com.github.vfyjxf.justenoughgraphs.api.gui.texture;

import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import org.jetbrains.annotations.NotNull;

/**
 * A texture that holds a reference to a component,
 * which can be generic to the component to adjust its own properties such as size.
 */
public interface IWidgetTexture extends IGuiTexture {

    @NotNull
    IGuiWidget getWidget();

}
