package com.github.vfyjxf.justenoughgraphs.config;

import org.eclipse.elk.graph.properties.IProperty;
import org.eclipse.elk.graph.properties.Property;

/**
 * Using ELK to store configuration data
 */
public class JeghOptions {

    private static final String HEADER = "com.github.vfyjxf.justenoughgraphs.";

    private static final Integer DEFAULT_COLOR = 0xFFFFFF;
    public static final IProperty<Integer> COLOR = new Property<>(
            HEADER + "color",
            DEFAULT_COLOR,
            null,
            null);

}
