package com.github.vfyjxf.justenoughgraphs.utils;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ErrorChecker {

    public static <T> T requireNonNull(@Nullable T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

    public static <X extends Throwable, T> T requireNonNull(@Nullable T obj, Supplier<? extends X> exceptionSupplier) throws X {
        if (obj == null) {
            throw exceptionSupplier.get();
        }
        return obj;
    }

    public static void checkElementIndex(int index, int size) {
        Preconditions.checkElementIndex(index, size);
    }


}
