package com.github.vfyjxf.justenoughgraphs.gui.screen;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.effects.OutlineEffect;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TestScreen  extends WindowScreen {

    private static final Color LINE_COLOR = new Color(255, 255, 255, 255);

    private final UIComponent cotainer = new UIContainer()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(15))
            .setWidth(new PixelConstraint(100))
            .setHeight(new PixelConstraint(100))
            .setChildOf(this.getWindow())
            .enableEffect(new OutlineEffect(Color.orange, 1));

    public static Screen openTest() {
        return new TestScreen();
    }


    public TestScreen() {
        super(ElementaVersion.V2);
        new UIText("Hello Elementa!")
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(10))
                .setColor(Color.CYAN)
                .setChildOf(cotainer);
    }

}
