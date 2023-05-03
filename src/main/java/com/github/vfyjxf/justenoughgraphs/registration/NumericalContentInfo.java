package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.INumericalContent;

import java.util.function.BiFunction;

public record NumericalContentInfo<T>(
        ContentType<T> contentType,
        BiFunction<T, Float, INumericalContent<T>> factory,
        IContentHelper<INumericalContent<T>> helper,
        IContentRenderer<INumericalContent<T>> renderer
) {
}
