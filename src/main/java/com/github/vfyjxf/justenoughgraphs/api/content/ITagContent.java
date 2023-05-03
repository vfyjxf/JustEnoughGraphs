package com.github.vfyjxf.justenoughgraphs.api.content;


import net.minecraft.tags.TagKey;

import java.util.List;

/**
 * @param <STACK> the stack type
 * @param <VALUE> the tag type
 */
public interface ITagContent<STACK, VALUE> extends IContent<STACK> {

    /**
     * @return the ContentType of the Stack of the type indicated by the tag.
     */
    @Override
    ContentType<STACK> getType();

    /**
     * @return the ContentType of the tag.
     */
    ContentType<VALUE> getTagType();

    /**
     * An IContent should avoid being called if it is an ITagContent,
     * but if it is called, the method should return a Content that represents the Tag.
     */
    @Override
    STACK getContent();

    TagKey<VALUE> getTag();

    List<VALUE> getContents();
}
