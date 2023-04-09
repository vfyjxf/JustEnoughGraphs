package com.github.vfyjxf.justenoughgraphs.api.content;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Collectors;

public interface IContentRenderer<T> {

    void render(PoseStack poseStack, T content);

    String getDisplayName(T content);

    List<Component> getTooltip(T content, TooltipFlag tooltipFlag);

    default List<ClientTooltipComponent> getClientTooltip(T content, TooltipFlag tooltipFlag) {
        return getTooltip(content, tooltipFlag)
                .stream()
                .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                .collect(Collectors.toList());
    }

    /**
     * Get the tooltip font renderer for this content.
     *
     * @param minecraft The minecraft instance.
     * @param content   The content to get the tooltip for.
     * @return The font renderer for the content.
     */
    default Font getFontRenderer(Minecraft minecraft, T content) {
        return minecraft.font;
    }

    /**
     * Get the width of the ingredient drawn on screen by this renderer.
     */
    default int getWidth() {
        return 16;
    }

    /**
     * Get the height of the ingredient drawn on screen by this renderer.
     */
    default int getHeight() {
        return 16;
    }

}
