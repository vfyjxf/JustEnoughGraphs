package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ICompositeContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.utils.CycleTimer;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeContent implements ICompositeContent {

    private final List<IContent<?>> contents;
    private final CycleTimer timer = new CycleTimer(20);
    private long amount;
    private float chance;

    public CompositeContent(Collection<?> contents, long amount, float chance) {
        this.amount = amount;
        this.chance = chance;
        this.contents = new ArrayList<>();
        IRegistryManager registryManager = IRegistryManager.getInstance();

        //group by class
        Map<ContentType<?>, List<IContent<?>>> grouped = contents.stream()
                .map(content -> registryManager.createContent(content, amount, chance))
                .collect(Collectors.groupingBy(
                        IContent::getType,
                        () -> new HashMap<ContentType<?>, List<IContent<?>>>(),
                        Collectors.toList()
                ));

        //sort by size
        grouped.values()
                .stream()
                .sorted(Comparator.comparingInt(List::size))
                .forEachOrdered(this.contents::addAll);

    }

    @Override
    public Object getContent() {
        return timer.getCycledValue(contents)
                .getContent();
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public void setChance(float chance) {
        ErrorChecker.checkRangeClosed(chance, 0, 1);
        this.chance = chance;
    }

    @Override
    public Map<ContentType<?>, List<IContent<?>>> getGrouped() {
        return contents.stream()
                .collect(Collectors.groupingBy(
                        IContent::getType,
                        () -> new HashMap<ContentType<?>, List<IContent<?>>>(),
                        Collectors.toList()
                ));
    }

    @Override
    public List<IContent<?>> getContents() {
        return new ArrayList<>(this.contents);
    }
}
