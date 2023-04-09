package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.api.content.IDescriptiveContent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

/**
 * Unit: game tick.
 */
public class TimeContent implements IDescriptiveContent<Long> {

    private final Long content;

    public TimeContent(Long content) {
        this.content = content;
    }

    @Override
    public ContentType<Long> getType() {
        return ContentTypes.DESCRIPTIVE_TIME_LONG;
    }

    @Override
    public ResourceLocation getTypeIdentifier() {
        return ContentTypes.DESCRIPTIVE_TIME_LONG.getIdentifier();
    }

    @Override
    public List<Component> getDescription() {
        Component description = Component.translatable("jegh.content.time.cost.description", content);
        return Collections.singletonList(description);
    }

    @Override
    public Long getContent() {
        return content;
    }

    @Override
    public long getAmount() {
        return content;
    }

    @Override
    public float getChance() {
        return 1.0f;
    }
}