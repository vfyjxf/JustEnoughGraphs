package com.github.vfyjxf.justenoughgraphs.elk;

import com.google.gson.JsonObject;
import org.eclipse.elk.graph.properties.IProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Layout options for saving elk locally
 */
public class LayoutOptions {

    private String id;
    private final Map<IProperty<?>, Object> options = new HashMap<>();

    public JsonObject toJson() {
        //TODO:
        JsonObject json = new JsonObject();
        return json;
    }


}
