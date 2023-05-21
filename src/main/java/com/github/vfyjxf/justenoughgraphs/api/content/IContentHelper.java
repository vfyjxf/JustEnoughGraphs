package com.github.vfyjxf.justenoughgraphs.api.content;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A Helper for handling Content.
 *
 * @param <T> The content type,it should be mutable.
 */
public interface IContentHelper<T> {

    String getModId(T toGet);

    String getIdentifier(T toGet,IdentifierContext type);

    CompoundTag serialize(T toSerialize);

    default CompoundTag serialize(IContent<T> toSerialize) {
        CompoundTag serialized = new CompoundTag();
        serialized.putString("identifier", toSerialize.getType().getIdentifier().toString());
        serialized.putLong("amount", toSerialize.getAmount());
        serialized.putFloat("chance", toSerialize.getChance());
        serialized.put("content", this.serialize(toSerialize.getContent()));
        return serialized;
    }

    @Nullable
    T deserialize(CompoundTag toDeserialize);

    @Nullable
    default IContent<T> deserializeContent(CompoundTag toDeserialize) {
        ResourceLocation identifier = new ResourceLocation(toDeserialize.getString("identifier"));
        long amount = toDeserialize.getLong("amount");
        float chance = toDeserialize.getFloat("chance");
        T content = this.deserialize(toDeserialize.getCompound("content"));
        IRegistryManager registryManager = IRegistryManager.getInstance();
        return registryManager.getContentType(identifier)
                .map(type -> registryManager.createContent(content, amount, chance))
                .orElse(null);
    }

    T copy(T toCopy);

    default IContent<T> copyContent(IContent<T> toCopy) {
        return IRegistryManager.getInstance()
                .createContent(toCopy.getContent(), toCopy.getAmount(), toCopy.getChance());
    }

    default T normalize(T toNormalize) {
        return copy(toNormalize);
    }

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


    default boolean matches(IContent<T> first, IContent<T> second) {
        return matches(first.getContent(), second.getContent()) && first.getChance() == second.getChance();
    }

    /**
     * Fuzzy matching, e.g. for ItemStack, it will only match Item.
     */
    boolean matchesFuzzy(T first, T second);

    default boolean matchesFuzzy(IContent<T> first, IContent<T> second) {
        return matchesFuzzy(first.getContent(), second.getContent());
    }


    /**
     * Merger is predominantly first content.
     * All types of Content should be mutable, except for some special Content.
     *
     * @return true if the two contents are merged.
     */
    boolean merge(T first, T second);

    default boolean mergeContent(IContent<T> first, IContent<T> second) {
        if (matches(first, second)) {
            first.setAmount(first.getAmount() + second.getAmount());
            return true;
        }
        return false;
    }

    boolean mergeFuzzy(T first, T second);

    default boolean mergeFuzzyContent(IContent<T> first, IContent<T> second) {
        if (matchesFuzzy(first, second)) {
            first.setAmount(first.getAmount() + second.getAmount());
            return true;
        }
        return false;
    }

    Collection<ResourceLocation> getTags(Collection<T> collection);

    default Collection<ResourceLocation> getContentTags(Collection<IContent<T>> collection) {
        return this.getTags(collection.stream().map(IContent::getContent).toList());
    }

}
