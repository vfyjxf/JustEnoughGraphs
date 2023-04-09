package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

public abstract class AbstractStackContent<T> implements IContent<T> {
    private final T content;
    private final long amount;
    private final float chance;

    public AbstractStackContent(T content, long amount, float chance) {
        this.content = content;
        this.amount = amount;
        this.chance = chance;
    }

    public AbstractStackContent(T content, long amount) {
        this.content = content;
        this.amount = amount;
        this.chance = 1.0f;
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
