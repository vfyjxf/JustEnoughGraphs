package com.github.vfyjxf.justenoughgraphs.api.content;

import java.util.List;

/**
 * A special Content containing an IContent object of a specific type.
 */
public interface IListContent<T> extends IContent<T> {

    List<IContent<T>> getContents();

}
