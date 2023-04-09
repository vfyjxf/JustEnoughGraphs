package com.github.vfyjxf.justenoughgraphs.helper;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.tags.ITagManager;

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

}
