package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IListContent;
import com.github.vfyjxf.justenoughgraphs.utils.CycleTimer;
import com.github.vfyjxf.justenoughgraphs.utils.ErrorChecker;

import java.util.ArrayList;
import java.util.List;

public class ListContent<T> implements IListContent<T> {


    private final List<IContent<T>> contents;
    private final CycleTimer timer = new CycleTimer(0);
    private long amount;
    private float chance;

    public ListContent(List<IContent<T>> contents, long amount, float chance) {
        this.contents = contents;
        this.amount = amount;
        this.chance = chance;
    }

    @Override
    public ContentType<T> getType() {
        return contents.stream()
                .map(IContent::getType)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No content type found"));
    }

    @Override
    public T getContent() {
        return timer.getCycledValue(contents).getContent();
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
    public List<IContent<T>> getContents() {
        return new ArrayList<>(contents);
    }
}
