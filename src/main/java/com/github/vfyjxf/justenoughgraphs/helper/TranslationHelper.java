package com.github.vfyjxf.justenoughgraphs.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TranslationHelper {

    public static String key(String children) {
        return "justenoughgraphs." + children;
    }

    public static MutableComponent translatable(String children, Object... args) {
        return Component.translatable(key(children), args);
    }

}
