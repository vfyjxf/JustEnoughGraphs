package com.github.vfyjxf.justenoughgraphs.content.tag;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ITagContent;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTagContent<STACK, VALUE> implements ITagContent<STACK, VALUE> {

    protected final TagKey<VALUE> tag;
    protected final List<VALUE> contents;
    protected long amount;
    protected float chance;

    protected AbstractTagContent(TagKey<VALUE> tag, List<VALUE> contents, long amount, float chance) {
        this.tag = tag;
        this.contents = contents;
        this.amount = amount;
        this.chance = chance;
    }

    protected AbstractTagContent(TagKey<VALUE> tag, List<VALUE> contents, long amount) {
        this(tag, contents, amount, 1.0f);
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
        this.chance = chance;
    }

    @Override
    public abstract ContentType<STACK> getType();

    @Override
    public abstract ContentType<VALUE> getTagType();

    @Override
    public abstract STACK getContent();

    @Override
    public TagKey<VALUE> getTag() {
        return tag;
    }

    @Override
    public List<VALUE> getContents() {
        return new ArrayList<>(contents);
    }
}
