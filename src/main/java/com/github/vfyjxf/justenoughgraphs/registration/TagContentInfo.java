package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.ITagFactory;

public record TagContentInfo<STACK, VALUE>(
        ContentType<VALUE> valueType,
        ContentType<STACK> stackType,
        ITagFactory<STACK, VALUE> factory,
        IContentRenderer<STACK> renderer
) {
}
