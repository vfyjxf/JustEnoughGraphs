package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IDescriptiveContent<T> extends IContent<T> {

    ResourceLocation getTypeIdentifier();

    List<Component> getDescription();

    interface IFactory<T> {
        IDescriptiveContent<T> create(T content);
    }

}
