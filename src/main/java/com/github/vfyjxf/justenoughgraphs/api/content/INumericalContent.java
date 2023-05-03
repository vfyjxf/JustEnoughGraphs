package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * A content that can be represented by a numerical value.
 * Such as time, energy, etc.
 * For very large numeric types, which usually exceed the size of the long type,
 * we agree that for numeric content, getAmount always returns 1.
 * For values less than long, returns a representation of the long type.
 */
public interface INumericalContent<T> extends IContent<T>, Comparable<INumericalContent<T>> {

    List<Component> getDescrictions();

    @Override
    ContentType<T> getType();

    @Override
    T getContent();

    @Override
    default long getAmount() {
        return 1;
    }

    @Override
    default void setAmount(long amount) {
        //NOOP
    }

    @Override
    float getChance();

    @Override
    void setChance(float chance);
}
