package com.github.vfyjxf.justenoughgraphs.content.renderer;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.api.content.ITagContent;
import com.github.vfyjxf.justenoughgraphs.helper.TranslationHelper;
import com.github.vfyjxf.justenoughgraphs.utils.NumberUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStackContentRenderer implements IContentRenderer<ItemStack> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void render(PoseStack poseStack, ItemStack content) {
        this.renderItemStack(poseStack, content, null);
    }

    @Override
    public void render(PoseStack poseStack, IContent<ItemStack> content) {
        ItemStack itemStack = content.getContent();
        String amount = content.getAmount() == 1 ? null : NumberUtils.toSuffix(content.getAmount());
        this.renderItemStack(poseStack, itemStack, amount);
    }

    @Override
    public void renderTag(PoseStack poseStack, ITagContent<ItemStack, ?> tag) {
        ItemStack content = tag.getContent();
        String amount = tag.getAmount() == 1 ? null : NumberUtils.toSuffix(tag.getAmount());
        this.renderItemStack(poseStack, content, amount);
    }

    private void renderItemStack(PoseStack poseStack, ItemStack content, @Nullable String text) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.mulPoseMatrix(poseStack.last().pose());

            RenderSystem.enableDepthTest();

            Minecraft minecraft = Minecraft.getInstance();
            Font font = getFontRenderer(minecraft, content);
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            itemRenderer.renderAndDecorateFakeItem(content, 0, 0);
            itemRenderer.renderGuiItemDecorations(font, content, 0, 0, text);
            RenderSystem.disableBlend();
        }
        modelViewStack.popPose();
        // Restore model-view matrix now that the item has been rendered
        RenderSystem.applyModelViewMatrix();
    }

    @Override
    public String getDisplayName(ItemStack content) {
        return content.getHoverName().getString();
    }

    @Override
    public List<Component> getTooltip(ItemStack content, TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        try {
            return content.getTooltipLines(player, tooltipFlag);
        } catch (RuntimeException | LinkageError e) {
            String itemStackInfo = ErrorUtil.getItemStackInfo(content);
            LOGGER.error("Failed to get tooltip: {}", itemStackInfo, e);
            List<Component> list = new ArrayList<>();
            MutableComponent crash = TranslationHelper.translatable("tooltip.error.crash");
            list.add(crash.withStyle(ChatFormatting.RED));
            return list;
        }
    }

    @Override
    public Font getFontRenderer(Minecraft minecraft, ItemStack content) {
        IClientItemExtensions renderProperties = IClientItemExtensions.of(content);
        Font fontRenderer = renderProperties.getFont(content, IClientItemExtensions.FontContext.TOOLTIP);
        if (fontRenderer != null) {
            return fontRenderer;
        }
        return minecraft.font;
    }

}
