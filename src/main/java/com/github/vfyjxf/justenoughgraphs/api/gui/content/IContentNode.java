package com.github.vfyjxf.justenoughgraphs.api.gui.content;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;

public interface IContentNode<T> extends IElkGraphComponent {

    IContent<T> getContent();

    IContentNode<T> setContent(IContent<T> content);


}
