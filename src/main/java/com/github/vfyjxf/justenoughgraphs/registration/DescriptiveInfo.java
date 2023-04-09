package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.IDescriptiveContent;
import net.minecraft.resources.ResourceLocation;

public record DescriptiveInfo<V, T extends IDescriptiveContent<V>>(
        ResourceLocation identifier,
        IContentHelper<T> helper,
        IContentRenderer<T> renderer,
        IDescriptiveContent.IFactory<T> factory
) {
}
