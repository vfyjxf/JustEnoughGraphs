package com.github.vfyjxf.justenoughgraphs.gui.textures;

import com.github.vfyjxf.justenoughgraphs.api.Globals;
import net.minecraft.resources.ResourceLocation;

public class GraphTextures {

    private static GraphTextures instance;

    public static GraphTextures getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GraphTextures has not been initialized!");
        }
        return instance;
    }

    private final JeghTextureManager textureManager;

    private final ResourceNineSliceTexture slotBackground;
    private final ResourceNineSliceTexture buttonDisabled;
    private final ResourceNineSliceTexture buttonEnabled;
    private final ResourceNineSliceTexture buttonHighlight;
    private final HighResolutionTexture graphIcon;

    public GraphTextures(JeghTextureManager textureManager) {
        this.textureManager = textureManager;
        {
            this.slotBackground = registerNineSliceGuiSprite("slot_background", 64, 64, 6, 6, 6, 6);
            this.buttonDisabled = registerNineSliceGuiSprite("button_disabled", 20, 20, 6, 6, 6, 6);
            this.buttonEnabled = registerNineSliceGuiSprite("button_enabled", 20, 20, 6, 6, 6, 6);
            this.buttonHighlight = registerNineSliceGuiSprite("button_highlight", 20, 20, 6, 6, 6, 6);
            ResourceTexture graphIcon = registerGuiSprite("icons/diagram_icon", 32, 32)
                    .trim(1, 2, 1, 1);
            this.graphIcon = new HighResolutionTexture(graphIcon, 4);
        }
        instance = this;
    }

    private ResourceLocation registerSprite(String name) {
        ResourceLocation location = new ResourceLocation(Globals.MOD_ID, name);
        textureManager.registerSprite(location);
        return location;
    }

    private ResourceTexture registerGuiSprite(String name, int width, int height) {
        ResourceLocation location = registerSprite(name);
        return new ResourceTexture(textureManager, location, width, height);
    }

    private ResourceNineSliceTexture registerNineSliceGuiSprite(String name, int width, int height, int left, int right, int top, int bottom) {
        ResourceLocation location = registerSprite(name);
        return new ResourceNineSliceTexture(textureManager, location, width, height, left, right, top, bottom);
    }

    public ResourceNineSliceTexture getButtonForState(boolean enabled, boolean hovered) {
        if (!enabled) {
            return buttonDisabled;
        } else if (hovered) {
            return buttonHighlight;
        } else {
            return buttonEnabled;
        }
    }

    public JeghTextureManager getTextureManager() {
        return textureManager;
    }

    public ResourceNineSliceTexture getSlotBackground() {
        return slotBackground;
    }

    public ResourceNineSliceTexture getButtonDisabled() {
        return buttonDisabled;
    }

    public ResourceNineSliceTexture getButtonEnabled() {
        return buttonEnabled;
    }

    public ResourceNineSliceTexture getButtonHighlight() {
        return buttonHighlight;
    }

    public HighResolutionTexture getGraphIcon() {
        return graphIcon;
    }

}
