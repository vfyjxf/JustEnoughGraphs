package com.github.vfyjxf.justenoughgraphs.registration;

public record SubTypeRecord<T, B>(Class<T> contentClass, Class<B> baseClass) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubTypeRecord<?, ?> that = (SubTypeRecord<?, ?>) o;

        if (!contentClass.equals(that.contentClass)) return false;
        return baseClass.equals(that.baseClass);
    }

    @Override
    public int hashCode() {
        int result = contentClass.hashCode();
        result = 31 * result + baseClass.hashCode();
        return result;
    }
}
