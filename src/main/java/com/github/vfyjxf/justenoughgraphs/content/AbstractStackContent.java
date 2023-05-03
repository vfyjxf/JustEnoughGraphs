package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;

public abstract class AbstractStackContent<T> implements IContent<T> {
    private final T content;
    private long amount;
    private float chance = 1.0f;

    public AbstractStackContent(T content, long amount, float chance) {
        this.content = content;
        this.amount = amount;
        this.chance = chance;
    }

    public AbstractStackContent(T content, long amount) {
        this.content = content;
        this.amount = amount;
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
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public void setChance(float chance) {
        ErrorChecker.checkRangeClosed(chance, 0, 1);
        this.chance = chance;
    }
}
