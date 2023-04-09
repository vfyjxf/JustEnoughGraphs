package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;

public record ContentInfo<T>(ContentType<T> contentType, IContentHelper<T> helper, IContentRenderer<T> renderer) {
}
