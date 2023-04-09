package com.github.vfyjxf.justenoughgraphs.gui.textures;


import com.github.vfyjxf.justenoughgraphs.api.Globals;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Based on {@link mezz.jei.common.gui.textures.Textures}.
 */
public class JeghTextureManager extends TextureAtlasHolder {
    private final Set<ResourceLocation> sprites = new HashSet<>();

    public JeghTextureManager(TextureManager textureManager) {
        super(textureManager, Globals.LOCATION_GRAPH_TEXTURE_ATLAS, "gui");
    }

    public void registerSprite(ResourceLocation location) {
        sprites.add(location);
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        return Collections.unmodifiableSet(sprites).stream();
    }

    @Override
    public TextureAtlasSprite getSprite(ResourceLocation location) {
        return super.getSprite(location);
    }
}
