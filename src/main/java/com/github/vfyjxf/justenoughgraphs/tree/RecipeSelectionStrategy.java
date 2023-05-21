package com.github.vfyjxf.justenoughgraphs.tree;

import com.github.vfyjxf.justenoughgraphs.api.Globals;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.helper.MathHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toSet;

public enum RecipeSelectionStrategy {
    /**
     * Recipe "actual consumption" minimum strategy, the "actual consumption" means the probabilistically calculated amount.
     */
    MINIMUM_CONSUME(new ResourceLocation(Globals.MOD_ID, "minimum_consumption_strategy"), (o1, o2) -> {
        long amount1 = o1.stream().mapToLong(MathHelper::getAmount).sum();
        long amount2 = o2.stream().mapToLong(MathHelper::getAmount).sum();
        return Long.compare(amount1, amount2);
    }),
    /**
     * Select the recipe that requires the least amount of different ingredients.
     */
    MINIMUM_VARIETY(new ResourceLocation(Globals.MOD_ID, "least_variety_strategy"), (o1, o2) -> {
        int variety1 = o1.stream().map(IContent::getType).collect(toSet()).size();
        int variety2 = o2.stream().map(IContent::getType).collect(toSet()).size();
        return Integer.compare(variety1, variety2);
    }),

    PREFER_PRIORITIZATION(new ResourceLocation(Globals.MOD_ID, "prefer_prioritization_strategy"), (o1, o2) -> {
        //TODO:Preference List not implemented.
        return 0;
    }),
    ;
    private final ResourceLocation uid;
    private final Comparator<List<IContent<?>>> strategy;

    RecipeSelectionStrategy(ResourceLocation uid, Comparator<List<IContent<?>>> strategy) {
        this.uid = uid;
        this.strategy = strategy;
    }

    public ResourceLocation getUid() {
        return uid;
    }

    public Comparator<List<IContent<?>>> getStrategy() {
        return strategy;
    }
}
