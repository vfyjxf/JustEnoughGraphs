package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.resources.ResourceLocation;

public class ContentType<T> {

    public static <T> ContentType<T> create(String namespace, String path, Class<? extends T> type) {
        ResourceLocation identifier = new ResourceLocation(namespace, path);
        return new ContentType<>(type, identifier);
    }

    private final Class<? extends T> type;
    private final ResourceLocation identifier;

    public ContentType(Class<? extends T> type, ResourceLocation identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public Class<? extends T> getTypeClass() {
        return type;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentType<?> that = (ContentType<?>) o;

        if (!type.equals(that.type)) return false;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ContentType{" +
                "type=" + type +
                ", identifier=" + identifier +
                '}';
    }
}
