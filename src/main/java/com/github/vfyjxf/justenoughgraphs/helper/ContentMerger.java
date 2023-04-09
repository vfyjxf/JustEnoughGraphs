package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IItemTagContent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentMerger {


    public static List<IContent<?>> mergeSameType(List<IContent<?>> contents) {
        return mergeHelper(contents);
    }

    @SuppressWarnings({"unchecked"})
    private static <T> List<IContent<?>> mergeHelper(List<IContent<?>> contents) {
        Map<ContentType<?>, List<IContent<?>>> group = new HashMap<>();
        List<IItemTagContent> tagContents = new ArrayList<>();
        for (IContent<?> content : contents) {
            if (content instanceof IItemTagContent) {
                tagContents.add((IItemTagContent) content);
                continue;
            }
            group.computeIfAbsent(content.getType(), k -> new ArrayList<>()).add(content);
        }
        List<IContent<?>> mergedContents = new ArrayList<>();
        for (var entry : group.entrySet()) {
            List<IContent<T>> contentList = (List) entry.getValue();
            mergedContents.addAll(merge(contentList));
        }
        mergedContents.addAll(mergeTag(tagContents));

        return mergedContents;
    }

    public static <T> List<IContent<T>> merge(List<IContent<T>> contents) {
        List<ContentPair<T>> mergedContents = new ArrayList<>();
        IRegistryManager registryManager = IRegistryManager.getInstance();
        IContentHelper<T> contentHelper = contents.stream().findFirst()
                .map(IContent::getType)
                .map(registryManager::getContentHelper)
                .orElseThrow(() -> new IllegalArgumentException("No content helper found"));
        for (IContent<T> content : contents) {
            boolean merged = false;
            for (ContentPair<T> mergedContent : mergedContents) {
                T maybeMerged = checkAndMerge(mergedContent, content, contentHelper);
                if (maybeMerged != null) {
                    merged = true;
                    mergedContent.setContent(maybeMerged);
                    break;
                }
            }
            if (!merged) {
                mergedContents.add(ContentPair.of(content.getContent(), content.getChance()));
            }
        }
        return mergedContents.stream()
                .map(pair -> registryManager.createContent(pair.getContent(), contentHelper.getAmount(pair.getContent()), pair.getChance()))
                .collect(Collectors.toList());
    }

    public static List<IItemTagContent> mergeTag(List<IItemTagContent> contents) {
        List<EditableItemTagPair> mergedContents = new ArrayList<>();
        IRegistryManager registryManager = IRegistryManager.getInstance();
        for (IItemTagContent content : contents) {
            boolean merged = false;
            for (EditableItemTagPair mergedContent : mergedContents) {
                if (mergedContent.getTag().equals(content.getTag())) {
                    float minChance = Math.min(mergedContent.getChance(), content.getChance());
                    mergedContent.setChance(minChance);
                    mergedContent.addAmount(content.getAmount());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                mergedContents.add(new EditableItemTagPair(content.getTag(), content.getAmount(), content.getChance()));
            }
        }
        return mergedContents.stream()
                .map(pair -> registryManager.createItemTagContent(pair.getTag().location(), pair.getAmount(), pair.getChance()))
                .collect(Collectors.toList());
    }

    @Nullable
    private static <T> T checkAndMerge(ContentPair<T> pair, IContent<T> second, IContentHelper<T> contentHelper) {
        T first = pair.getContent();
        T secondContent = second.getContent();
        if (contentHelper.matchesFuzzy(first, secondContent)) {
            boolean isFirstParent = pair.getChance() >= second.getChance();
            return isFirstParent ? contentHelper.merge(first, secondContent) : contentHelper.merge(secondContent, first);
        }
        return null;
    }

    private static class ContentPair<T> {
        private T content;
        private float chance;

        public static <T> ContentPair<T> of(T content, float chance) {
            return new ContentPair<>(content, chance);
        }

        public ContentPair(T content, float chance) {
            this.content = content;
            this.chance = chance;
        }

        public T getContent() {
            return content;
        }

        public void setContent(T content) {
            this.content = content;
        }

        public float getChance() {
            return chance;
        }

        public void setChance(float chance) {
            this.chance = chance;
        }
    }

    private static class EditableItemTagPair {
        private TagKey<Item> tag;
        private long amount;
        private float chance;

        public EditableItemTagPair(TagKey<Item> tag, long amount, float chance) {
            this.tag = tag;
            this.amount = amount;
            this.chance = chance;
        }

        public TagKey<Item> getTag() {
            return tag;
        }

        public void setTag(TagKey<Item> tag) {
            this.tag = tag;
        }

        public float getChance() {
            return chance;
        }

        public void setChance(float chance) {
            this.chance = chance;
        }

        public long getAmount() {
            return amount;
        }

        public void addAmount(long amount) {
            this.amount += amount;
        }
    }

}
