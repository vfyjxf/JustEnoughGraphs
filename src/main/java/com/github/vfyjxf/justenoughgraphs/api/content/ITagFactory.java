package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ITagFactory<T, V> {

    @Nullable
    ITagContent<T, V> create(ResourceLocation tag, long amount, float chance);

}
