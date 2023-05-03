package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.api.content.INumericalContent;
import com.github.vfyjxf.justenoughgraphs.helper.TranslationHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ForgeEnergyContent extends NumericalContent<Long> {

    private final List<Component> descriptions;

    public ForgeEnergyContent(Long content) {
        super(content);
        Component description = TranslationHelper.translatable("jegh.content.energy.fe.cost.description", content);
        descriptions = Collections.singletonList(description);
    }

    @Override
    public List<Component> getDescrictions() {
        return descriptions;
    }

    @Override
    public ContentType<Long> getType() {
        return ContentTypes.NUMERICAL_ENERGY_LONG;
    }

    @Override
    public Long getContent() {
        return content;
    }

    @Override
    public int compareTo(@NotNull INumericalContent<Long> o) {
        return Long.compare(content, o.getContent());
    }
}
