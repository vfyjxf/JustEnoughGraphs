package com.github.vfyjxf.justenoughgraphs.content.renderer;

import com.github.vfyjxf.justenoughgraphs.api.content.ICompositeContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CompositeContentRenderer implements IContentRenderer<ICompositeContent> {
    @Override
    public void render(PoseStack poseStack, ICompositeContent content) {

    }

    @Override
    public String getDisplayName(ICompositeContent content) {
        return null;
    }

    @Override
    public List<Component> getTooltip(ICompositeContent content, TooltipFlag tooltipFlag) {
        return null;
    }
}
