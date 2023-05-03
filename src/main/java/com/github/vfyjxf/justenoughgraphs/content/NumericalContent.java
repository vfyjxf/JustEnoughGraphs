package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.INumericalContent;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class NumericalContent<T> implements INumericalContent<T> {

    protected final T content;

    public NumericalContent(T content) {
        this.content = content;
    }

    @Override
    public abstract ContentType<T> getType();

    @Override
    public abstract List<Component> getDescrictions();

    @Override
    public T getContent() {
        return content;
    }

    @Override
    public long getAmount() {
        if (content instanceof Number number) {
            return number.longValue();
        }
        return 0;
    }

    @Override
    public void setAmount(long amount) {
        //NOOP
    }

    @Override
    public float getChance() {
        return 1.0f;
    }

    @Override
    public void setChance(float chance) {
        //NOOP
    }
}
