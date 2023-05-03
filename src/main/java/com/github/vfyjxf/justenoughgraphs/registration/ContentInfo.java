package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentFactory;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;

public record ContentInfo<T>(
        ContentType<T> contentType,
        IContentFactory<T> factory,
        IContentHelper<T> helper,
        IContentRenderer<T> renderer
) {
}
