package com.github.vfyjxf.justenoughgraphs.api.content;

import java.util.List;
import java.util.Map;

/**
 * A list type Content that can hold multiple types.
 */
public interface ICompositeContent extends IContent {

    @Override
    default ContentType getType() {
        return ContentTypes.COMPOSITE;
    }

    Map<ContentType<?>, List<IContent<?>>> getGrouped();

    List<IContent<?>> getContents();

}
