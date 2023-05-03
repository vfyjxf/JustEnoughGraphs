package com.github.vfyjxf.justenoughgraphs.api.content;

public interface IContent<T> {

    ContentType<T> getType();

    /**
     * For TagContent, it should be the content that can represent this tag.
     */
    T getContent();

    long getAmount();

    void setAmount(long amount);

    float getChance();

    void setChance(float chance);

}
