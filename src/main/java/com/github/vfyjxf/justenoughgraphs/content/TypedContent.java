package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

public class TypedContent<T> implements IContent<T> {

    private final ContentType<T> type;
    private final T content;
    private final long amount;
    private final float chance;

    public TypedContent(ContentType<T> type, T content, long amount, float chance) {
        this.type = type;
        this.content = content;
        this.amount = amount;
        this.chance = chance;
    }

    @Override
    public ContentType<T> getType() {
        return type;
    }

    @Override
    public T getContent() {
        return content;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public float getChance() {
        return chance;
    }
}
