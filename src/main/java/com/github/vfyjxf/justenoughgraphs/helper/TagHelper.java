package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.ITagContent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagHelper {

    public static <VALUE, STACK> Optional<ResourceLocation> getFirstTagEquivalent(
            Collection<STACK> stacks,
            Function<STACK, VALUE> stackToValue,
            Supplier<ITagManager<VALUE>> tagSupplier
    ) {
        return getTags(stacks, stackToValue, tagSupplier)
                .findFirst();
    }

    public static <VALUE, STACK> Collection<ResourceLocation> getTagEquivalents(
            Collection<STACK> stacks,
            Function<STACK, VALUE> stackToValue,
            Supplier<ITagManager<VALUE>> tagSupplier
    ) {
        return getTags(stacks, stackToValue, tagSupplier)
                .collect(Collectors.toList());
    }

    private static <VALUE, STACK> Stream<ResourceLocation> getTags(
            Collection<STACK> stacks,
            Function<STACK, VALUE> stackToValue,
            Supplier<ITagManager<VALUE>> tagSupplier
    ) {
        if (stacks.size() < 2) {
            return Stream.empty();
        }

        List<VALUE> values = stacks.stream()
                .map(stackToValue)
                .toList();

        ITagManager<VALUE> tagManager = tagSupplier.get();

        return tagManager.getTagNames()
                .map(tagManager::getTag)
                .filter(tag -> values.stream().allMatch(tag::contains))
                .map(tag -> tag.getKey().location());
    }

    public static <STACK> String getNamespace(ITagContent<STACK, ?> toGet) {
        return toGet.getTag().location().getNamespace();
    }

    public static <STACK> String getPath(ITagContent<STACK, ?> toGet) {
        return toGet.getTag().location().getPath();
    }

    public static <STACK> CompoundTag serialize(ITagContent<STACK, ?> toGet) {
        CompoundTag serialized = new CompoundTag();
        ResourceLocation tag = toGet.getTag().location();
        serialized.putString("stackType", toGet.getType().getIdentifier().getNamespace());
        serialized.putString("tag", tag.toString());
        serialized.putLong("amount", toGet.getAmount());
        serialized.putFloat("chance", toGet.getChance());
        return serialized;
    }

    @Nullable
    public static <STACK> ITagContent<STACK, ?> deserialize(CompoundTag toDeserialize) {
        ResourceLocation tag = new ResourceLocation(toDeserialize.getString("tag"));
        long amount = toDeserialize.getLong("amount");
        float chance = toDeserialize.getFloat("chance");
        ResourceLocation identifier = new ResourceLocation(toDeserialize.getString("stackType"));
        IRegistryManager manager = IRegistryManager.getInstance();
        return (ITagContent<STACK, ?>) manager.getContentType(identifier)
                .map(type -> manager.createTagContent(type, tag, amount, chance))
                .orElse(null);
    }

}
