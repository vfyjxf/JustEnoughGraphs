package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.api.content.INumericalContent;
import com.github.vfyjxf.justenoughgraphs.helper.TranslationHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Unit: game tick.
 */
public class TimeContent extends NumericalContent<Long> {

    private final List<Component> descriptions;

    public TimeContent(Long content) {
        super(content);
        Component description = TranslationHelper.translatable("jegh.content.time.tick.cost.description", content);
        descriptions = Collections.singletonList(description);
    }

    @Override
    public ContentType<Long> getType() {
        return ContentTypes.NUMERICAL_TIME;
    }

    @Override
    public List<Component> getDescrictions() {
        return descriptions;
    }

    public List<Component> getDescription() {
        Component description = TranslationHelper.translatable("jegh.content.time.cost.description", content);
        return Collections.singletonList(description);
    }

    @Override
    public int compareTo(@NotNull INumericalContent<Long> o) {
        return Long.compare(content, o.getContent());
    }

}
