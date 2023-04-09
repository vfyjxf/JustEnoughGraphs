package com.github.vfyjxf.justenoughgraphs.api.content;

public interface IContent<T> {

    ContentType<T> getType();

    T getContent();

    long getAmount();

    float getChance();

}
