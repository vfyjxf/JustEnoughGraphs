package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface IContentHelper<T> {

    String getModId(T toGet);

    CompoundTag serialize(T toSerialize);

    @Nullable
    T deserialize(CompoundTag toDeserialize);

    T copy(T toCopy);

    /**
     * Get the amount of the content.
     * If a content has no amount, return -1
     */
    long getAmount(T toGet);

    T setAmount(T toSet, long amount);

    @Nullable
    CompoundTag getTag(T toGet);

    /**
     * @return true if the two contents are the same.
     * It does not match the amount.
     */
    boolean matches(T first, T second);

    boolean matchesFuzzy(T first, T second);


    /**
     * Merger is predominantly first content.
     *
     * @return the merged content, or null if the merge failed.
     */
    @Nullable
    T merge(T first, T second);

    @Nullable
    T mergeFuzzy(T first, T second);

    Collection<ResourceLocation> getTags(Collection<T> collection);

}
