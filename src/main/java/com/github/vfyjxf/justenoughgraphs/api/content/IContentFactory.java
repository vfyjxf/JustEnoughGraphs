package com.github.vfyjxf.justenoughgraphs.api.content;


@FunctionalInterface
public interface IContentFactory<T> {

    IContent<T> create(T content, long amount, float chance);

    default IContent<T> create(T content, long amount) {
        return create(content, amount, 1.0f);
    }

}
